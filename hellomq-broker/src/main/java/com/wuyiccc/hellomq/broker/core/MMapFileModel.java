package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.constants.BrokerConstants;
import com.wuyiccc.hellomq.broker.model.CommitLogMessageModel;
import com.wuyiccc.hellomq.broker.model.CommitLogModel;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;
import com.wuyiccc.hellomq.broker.utils.CommitLogFileNameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Objects;

/**
 * @author wuyiccc
 * @date 2024/8/25 21:35
 * 最基础的mmap对象模型
 */
public class MMapFileModel {

    /**
     * 映射的文件
     */
    private File file;

    private MappedByteBuffer mappedByteBuffer;

    private FileChannel fileChannel;

    private String topicName;

    /**
     * 指定offset做文件的映射
     *
     * @param topicName   topic名称
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积 (byte)
     * @throws IOException
     */
    public void loadFileInMMap(String topicName, int startOffset, int mappedSize) throws IOException {

        this.topicName = topicName;
        String filePath = getLatestCommitLogFile(topicName);
        doMMap(filePath, startOffset, mappedSize);
    }


    /**
     * 获取最新commitLog文件路径
     */
    private String getLatestCommitLogFile(String topicName) {

        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(topicName);
        if (mqTopicModel == null) {
            throw new IllegalArgumentException("topic is inValid topicName is " + topicName);
        }

        CommitLogModel commitLogModel = mqTopicModel.getCommitLogModel();

        long diff = commitLogModel.getOffsetLimit() - commitLogModel.getOffset();

        String filePath = null;
        if (diff == 0) {
            // 已经写满了
            filePath = createNewCommitLogFile(topicName, commitLogModel);
        } else if (diff > 0) {
            filePath = CommonCache.getGlobalProperties().getHelloMqHome()
                    + BrokerConstants.BASE_STORE_PATH
                    + topicName
                    + commitLogModel.getFileName();
        }
        return filePath;
    }

    private String createNewCommitLogFile(String topicName, CommitLogModel commitLogModel) {
        String newFileName = CommitLogFileNameUtils.incrCommitLogFileName(commitLogModel.getFileName());
        String newFilePath = CommonCache.getGlobalProperties().getHelloMqHome()
                + BrokerConstants.BASE_STORE_PATH
                + topicName
                + newFileName;

        File newCommitLogFile = new File(newFilePath);
        try {
            newCommitLogFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newFilePath;
    }

    /**
     * 支持从文件的指定offset开始读取内容
     *
     * @param readOffset 开始位置
     * @param size       内容大小
     */
    public byte[] readContent(int readOffset, int size) {

        this.mappedByteBuffer.position(readOffset);
        byte[] content = new byte[size];

        int j = 0;
        for (int i = 0; i < size; i++) {
            // 这里是从内存中访问
            byte b = this.mappedByteBuffer.get(readOffset + i);
            content[j++] = b;
        }

        return content;
    }

    /**
     * 写内容到磁盘上, 默认不强制刷盘
     *
     * @param commitLogMessageModel 文件内容
     */
    public void writeContent(CommitLogMessageModel commitLogMessageModel) throws IOException {
        this.writeContent(commitLogMessageModel, false);
    }

    /**
     * 写入数据到磁盘中
     *
     * @param commitLogMessageModel 数据内容
     * @param force                 是否强制刷盘
     */
    public void writeContent(CommitLogMessageModel commitLogMessageModel, boolean force) throws IOException {

        // 定位到最新的commitLog文件中, 记录下当前文件是否已经写满, 如果写满，则创建新的文件，并且做新的mmap映射
        // 如果当前文件没有写满, 对content内容做一层封装, 再判断写入是否会导致commitLog写满, 如果不会，则选择当前commitLog, 如果会则创建新文件，并且做mmap映射
        // 定位到最新的commitLog文件之后, 写入
        // 定义一个对象专门管理各个topic的最新写入offset值, 并且定时刷新到磁盘中
        // 写入数据, offset变更, 如果是高并发场景, offset是不是会被多个线程访问


        // 默认刷到pageCache中
        // 如果需要强制刷盘, 这里要兼容
        this.checkCommitLogHasEnableSpace(commitLogMessageModel);

        this.mappedByteBuffer.put(commitLogMessageModel.convertToByte());
        if (force) {
            this.mappedByteBuffer.force();
        }
    }


    private void checkCommitLogHasEnableSpace(CommitLogMessageModel commitLogMessageModel) throws IOException {


        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(this.topicName);
        CommitLogModel commitLogModel = mqTopicModel.getCommitLogModel();
        long writeAbleOffsetNum = commitLogModel.getOffsetLimit() - commitLogModel.getOffset();

        if (writeAbleOffsetNum < commitLogMessageModel.getSize()) {
            // 空间不足需要创建新的commitLog文件并且做映射
            // 0000000000 文件  -> 00000001文件
            String newFilePath = this.createNewCommitLogFile(topicName, commitLogModel);

            this.doMMap(newFilePath, 0, BrokerConstants.COMMITLOG_DEFAULT_MMAP_SIZE);
        }

    }


    /**
     * 执行mmap步骤
     */
    private void doMMap(String filePath, int startOffset, int mappedSize) throws IOException {

        file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }
        this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
    }

    /**
     * 释放DirectByteBuffer的mapped直接内存空间, 可以通过arthas来观察maaped内存释放释放, 参考rocketmq的MappedFile#clean()方法
     */
    public void clean() {

        if (Objects.isNull(this.mappedByteBuffer) || !this.mappedByteBuffer.isDirect() || this.mappedByteBuffer.capacity() == 0) {
            return;
        }
        invoke(invoke(viewed(this.mappedByteBuffer), "cleaner"), "clean");
    }

    private static Object invoke(final Object target, final String methodName, final Class<?>... args) {
        return AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            try {
                Method method = method(target, methodName, args);
                method.setAccessible(true);
                return method.invoke(target);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private static Method method(Object target, String methodName, Class<?>[] args) throws NoSuchMethodException {

        try {
            return target.getClass().getMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            return target.getClass().getDeclaredMethod(methodName, args);
        }
    }

    private static ByteBuffer viewed(ByteBuffer buffer) {
        String methodName = "viewedBuffer";
        Method[] methods = buffer.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equals("attachment")) {
                methodName = "attachment";
                break;
            }
        }

        ByteBuffer viewedBuffer = (ByteBuffer) invoke(buffer, methodName);
        if (Objects.isNull(viewedBuffer)) {
            return buffer;
        } else {
            return viewed(viewedBuffer);
        }
    }

}

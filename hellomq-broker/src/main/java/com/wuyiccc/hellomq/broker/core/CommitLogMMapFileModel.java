package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import com.wuyiccc.hellomq.broker.model.*;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.broker.utils.LogFileNameUtils;
import com.wuyiccc.hellomq.broker.utils.PutMessageLock;
import com.wuyiccc.hellomq.broker.utils.UnfairReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuyiccc
 * @date 2024/8/25 21:35
 * 最基础的mmap对象模型
 */
public class CommitLogMMapFileModel {

    private static final Logger log = LoggerFactory.getLogger(CommitLogMMapFileModel.class);

    /**
     * 映射的文件
     */
    private File file;

    private MappedByteBuffer mappedByteBuffer;

    private FileChannel fileChannel;

    private String topicName;

    private PutMessageLock putMessageLock;

    private ByteBuffer readByteBuffer;

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
        // 默认非公平模式
        putMessageLock = new UnfairReentrantLock();
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

        long diff = commitLogModel.countDiff();

        String filePath = null;
        if (diff == 0) {
            // 已经写满了
            CommitLogFilePath newCommitLogFile = createNewCommitLogFile(topicName, commitLogModel);
            filePath = newCommitLogFile.getFilePath();
        } else if (diff > 0) {
            filePath = LogFileNameUtils.buildCommitLogFilePath(topicName, commitLogModel.getFileName());
        }
        return filePath;
    }

    private CommitLogFilePath createNewCommitLogFile(String topicName, CommitLogModel commitLogModel) {
        String newFileName = LogFileNameUtils.incrCommitLogFileName(commitLogModel.getFileName());
        String newFilePath = LogFileNameUtils.buildCommitLogFilePath(topicName, newFileName);

        File newCommitLogFile = new File(newFilePath);
        try {
            newCommitLogFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new CommitLogFilePath(newFilePath, newFileName);
    }

    /**
     * 支持从文件的指定offset开始读取内容
     */
    public byte[] readContent(int pos, int len) {

        ByteBuffer readBuf = readByteBuffer.slice();
        readBuf.position(pos);
        byte[] readBytes = new byte[len];
        readBuf.get(readBytes);
        return readBytes;
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
        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(topicName);
        if (mqTopicModel == null) {
            throw new IllegalArgumentException("mqTopicModel is null");
        }
        CommitLogModel commitLogModel = mqTopicModel.getCommitLogModel();
        if (commitLogModel == null) {
            throw new IllegalArgumentException("commitLogModel is null");
        }


        // lock()
        putMessageLock.lock();
        this.checkCommitLogHasEnableSpace(commitLogMessageModel);
        byte[] bytes = commitLogMessageModel.convertToByte();
        this.mappedByteBuffer.put(bytes);
        AtomicInteger currentLatestOffset = commitLogModel.getOffset();
        this.dispatcher(bytes, currentLatestOffset.get());
        currentLatestOffset.addAndGet(bytes.length);

        // 默认刷到pageCache中
        // 如果需要强制刷盘, 这里要兼容
        if (force) {
            this.mappedByteBuffer.force();
        }
        // unlock()
        putMessageLock.unLock();
    }


    /**
     * 将consumerQueue文件写入
     */
    private void dispatcher(byte[] writeContent, int msgIndex) {
        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(topicName);
        if (mqTopicModel == null) {
            throw new IllegalArgumentException("topic is undefined");
        }

        // todo(wuyiccc): queueId这些暂时写死
        int queueId = 0;

        String fileName = mqTopicModel.getCommitLogModel().getFileName();

        ConsumeQueueDetailModel consumeQueueDetailModel = new ConsumeQueueDetailModel();
        consumeQueueDetailModel.setCommitLogFileName(Integer.parseInt(fileName));
        consumeQueueDetailModel.setMsgIndex(msgIndex);
        consumeQueueDetailModel.setMsgLength(writeContent.length);
        log.info("写入consumequeue内容: " + JsonUtils.toJsonStr(consumeQueueDetailModel, true));
        byte[] content = consumeQueueDetailModel.convertToBytes();
        consumeQueueDetailModel.buildFromBytes(content);
        log.info("byte convert is : " + JsonUtils.toJsonStr(consumeQueueDetailModel, true));
        List<ConsumeQueueMMapFileModel> queueModelList = CommonCache.getConsumeQueueMMapFileModelManager().get(topicName);
        ConsumeQueueMMapFileModel consumeQueueMMapFileModel = queueModelList.stream()
                .filter(queueModel -> queueModel.getQueueId().equals(queueId))
                .findFirst().orElse(null);
        consumeQueueMMapFileModel.writeContent(content);

        // 刷新offset
        QueueModel queueModel = mqTopicModel.getQueueList().get(queueId);
        queueModel.getLatestOffset().addAndGet(content.length);
    }



    private void checkCommitLogHasEnableSpace(CommitLogMessageModel commitLogMessageModel) throws IOException {


        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(this.topicName);
        CommitLogModel commitLogModel = mqTopicModel.getCommitLogModel();
        long writeAbleOffsetNum = commitLogModel.countDiff();

        if (writeAbleOffsetNum < commitLogMessageModel.convertToByte().length) {
            // 空间不足需要创建新的commitLog文件并且做映射
            // 0000000000 文件  -> 00000001文件
            CommitLogFilePath newCommitLogFile = this.createNewCommitLogFile(topicName, commitLogModel);
            commitLogModel.setOffsetLimit(Long.valueOf(BrokerConstants.COMMITLOG_DEFAULT_MMAP_SIZE));
            commitLogModel.setOffset(new AtomicInteger(0));
            commitLogModel.setFileName(newCommitLogFile.getFileName());

            this.doMMap(newCommitLogFile.getFilePath(), 0, BrokerConstants.COMMITLOG_DEFAULT_MMAP_SIZE);
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
        this.readByteBuffer = mappedByteBuffer.slice();
        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(topicName);
        // 末尾数据追加
        this.mappedByteBuffer.position(mqTopicModel.getCommitLogModel().getOffset().get());
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

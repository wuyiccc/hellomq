package com.wuyiccc.hellomq.broker.utils;

import com.wuyiccc.hellomq.broker.constants.BrokerConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * @author wuyiccc
 * @date 2024/8/25 11:01
 * 支持基于java的MMap api访问文件的能力 (文件的读写)
 * 支持指定的offset的文件映射 (end offset - start offset = 映射的内存体积)
 * 文件从指定的offset开始读取
 * 文件从指定的offset开始写入
 * 文件映射后的内存释放
 */
public class MMapUtils {

    /**
     * 映射的文件
     */
    private File file;

    private MappedByteBuffer mappedByteBuffer;

    private FileChannel fileChannel;


    /**
     * 指定offset做文件的映射
     *
     * @param filePath    文件路径
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积 (byte)
     * @throws IOException
     */
    public void loadFileInMMap(String filePath, int startOffset, int mappedSize) throws IOException {

        this.file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }

        this.fileChannel = new RandomAccessFile(file, "rw").getChannel();

        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
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
     * @param content 文件内容
     */
    public void writeContent(byte[] content) {
        this.writeContent(content, false);
    }

    /**
     * 写入数据到磁盘中
     *
     * @param content 数据内容
     * @param force   是否强制刷盘
     */
    public void writeContent(byte[] content, boolean force) {
        // 默认刷到pageCache中
        // 如果需要强制刷盘, 这里要兼容
        this.mappedByteBuffer.put(content);
        if (force) {
            this.mappedByteBuffer.force();
        }
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


    public static void main(String[] args) throws IOException, InterruptedException {

        MMapUtils mMapUtils = new MMapUtils();
        mMapUtils.loadFileInMMap("/Users/wuxingyu/work/code_learn/031-opensource/14_hellomq/hellomq/data/broker/store/test_topic/00000000"
                , 0
                , BrokerConstants.COMMITLOG_DEFAULT_MMAP_SIZE);
        CountDownLatch count = new CountDownLatch(1);
        CountDownLatch allWriteSuccess = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread task = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        count.await();
                        mMapUtils.writeContent(("test-content-" + finalI).getBytes(StandardCharsets.UTF_8));
                        allWriteSuccess.countDown();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            task.start();
        }

        System.out.println("准备执行并发写入mmap测试");
        count.countDown();
        allWriteSuccess.await();
        System.out.println("并发测试写入完毕");

        byte[] content = mMapUtils.readContent(0, BrokerConstants.COMMITLOG_DEFAULT_MMAP_SIZE);
        System.out.println("内容: " + new String(content));

    }


}

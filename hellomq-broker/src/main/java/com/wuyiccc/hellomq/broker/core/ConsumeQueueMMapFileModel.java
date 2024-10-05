package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;
import com.wuyiccc.hellomq.broker.model.QueueModel;
import com.wuyiccc.hellomq.broker.utils.LogFileNameUtils;
import com.wuyiccc.hellomq.broker.utils.PutMessageLock;
import com.wuyiccc.hellomq.broker.utils.UnfairReentrantLock;
import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * @author wuyiccc
 * @date 2024/9/23 21:37
 * <p>
 * 对consumeQueue文件作mmap映射的核心对象
 */
public class ConsumeQueueMMapFileModel {

    private static final Logger log = LoggerFactory.getLogger(ConsumeQueueMMapFileModel.class);



    /**
     * 映射的文件
     */
    private File file;

    private MappedByteBuffer mappedByteBuffer;

    private FileChannel fileChannel;

    private String topicName;

    private Integer queueId;

    private String consumeQueueFileName;

    private PutMessageLock putMessageLock;

    private ByteBuffer readBuffer;


    /**
     * 指定offset做文件的映射
     *
     * @param topicName   topic名称
     * @param queueId     队列id
     * @param startOffset 开始映射的offset
     * @param mappedSize  映射的体积 (byte)
     * @throws IOException
     */
    public void loadFileInMMap(String topicName
            , Integer queueId
            , int startOffset
            , int latestWriteOffset
            , int mappedSize
    ) throws IOException {

        this.topicName = topicName;
        this.queueId = queueId;
        String filePath = getLatestCommitLogFile();

        this.doMMap(filePath, startOffset, latestWriteOffset, mappedSize);

        this.putMessageLock = new UnfairReentrantLock();
    }

    private void doMMap(String filePath, int startOffset, int latestWriteOffset, int mappedSize) throws IOException {
        file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("filePath is " + filePath + " inValid");
        }
        this.fileChannel = new RandomAccessFile(file, "rw").getChannel();
        this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, startOffset, mappedSize);
        this.readBuffer = mappedByteBuffer.slice();
        this.mappedByteBuffer.position(latestWriteOffset);
    }


    /**
     * 获取最新commitLog文件路径
     */
    private String getLatestCommitLogFile() {

        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(topicName);
        if (mqTopicModel == null) {
            throw new IllegalArgumentException("topic is inValid topicName is " + topicName);
        }

        List<QueueModel> queueList = mqTopicModel.getQueueList();
        QueueModel queueModel = queueList.get(queueId);

        if (queueModel == null) {
            throw new IllegalArgumentException("queueId is inValid! queueId is " + queueId);
        }

        int diff = queueModel.getOffsetLimit();
        String filePath = null;
        if (diff == 0) {
            filePath = this.createNewCommitLogFile(queueModel.getFileName());
        } else {
            filePath = LogFileNameUtils.buildConsumeQueueFilePath(topicName, queueId, queueModel.getFileName());
        }
        return filePath;
    }

    private String createNewCommitLogFile(String fileName) {

        String newFileName = LogFileNameUtils.incrConsumeQueueFileName(fileName);
        String newFilePath = LogFileNameUtils.buildConsumeQueueFilePath(topicName, queueId, newFileName);

        File newCommitLogFile = new File(newFilePath);
        try {
            newCommitLogFile.createNewFile();
            log.info("创建了新的consumeQueue文件");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newFilePath;
    }

    public void writeContent(byte[] content, boolean force) {

        try {
            putMessageLock.lock();
            mappedByteBuffer.put(content);
            if (force) {
                mappedByteBuffer.force();
            }
        } finally {
            putMessageLock.unLock();
        }
    }

    public void writeContent(byte[] content) {

        writeContent(content, false);
    }

    public byte[] readContent(int pos) {

        ByteBuffer readBuf = readBuffer.slice();
        readBuf.position(pos);
        byte[] content = new byte[BrokerConstants.CONSUME_CONTENT_READ_LENGTH];
        readBuf.get(content);
        return content;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public MappedByteBuffer getMappedByteBuffer() {
        return mappedByteBuffer;
    }

    public void setMappedByteBuffer(MappedByteBuffer mappedByteBuffer) {
        this.mappedByteBuffer = mappedByteBuffer;
    }

    public FileChannel getFileChannel() {
        return fileChannel;
    }

    public void setFileChannel(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Integer getQueueId() {
        return queueId;
    }

    public void setQueueId(Integer queueId) {
        this.queueId = queueId;
    }

    public String getConsumeQueueFileName() {
        return consumeQueueFileName;
    }

    public void setConsumeQueueFileName(String consumeQueueFileName) {
        this.consumeQueueFileName = consumeQueueFileName;
    }

    public PutMessageLock getPutMessageLock() {
        return putMessageLock;
    }

    public void setPutMessageLock(PutMessageLock putMessageLock) {
        this.putMessageLock = putMessageLock;
    }
}

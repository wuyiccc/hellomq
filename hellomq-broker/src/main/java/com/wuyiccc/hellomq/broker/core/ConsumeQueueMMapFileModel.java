package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.utils.PutMessageLock;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author wuyiccc
 * @date 2024/9/23 21:37
 * <p>
 * 对consumeQueue文件作mmap映射的核心对象
 */
public class ConsumeQueueMMapFileModel {


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
            , String consumeQueueFileName
            , int startOffset
            , int mappedSize
    ) throws IOException {

        this.topicName = topicName;
        this.queueId = queueId;
        this.consumeQueueFileName = consumeQueueFileName;

    }

}

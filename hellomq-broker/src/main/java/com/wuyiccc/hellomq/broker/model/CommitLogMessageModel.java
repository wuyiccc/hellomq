package com.wuyiccc.hellomq.broker.model;

import com.wuyiccc.hellomq.broker.utils.ByteConvertUtils;

import java.util.Arrays;

/**
 * @author wuyiccc
 * @date 2024/9/3 22:27
 *
 * commitLog真实数据存储对象模型
 */
public class CommitLogMessageModel {


    /**
     * 消息的体积大小, 单位是字节
     */
    private int size;


    /**
     * 真正的消息内容
     */
    private byte[] content;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommitLogMessageModel{" +
                "size=" + size +
                ", content=" + Arrays.toString(content) +
                '}';
    }

    public byte[] convertToByte() {
        byte[] sizeBytes = ByteConvertUtils.intToBytes(getSize());
        byte[] content = getContent();
        byte[] mergeResultByte = new byte[sizeBytes.length + content.length];

        int j = 0;
        for (byte sizeByte : sizeBytes) {
            mergeResultByte[j] = sizeByte;
            j++;
        }

        for (byte b : content) {
            mergeResultByte[j] = b;
            j++;
        }

        return mergeResultByte;
    }
}

package com.wuyiccc.hellomq.broker.model;

import java.util.Arrays;

/**
 * @author wuyiccc
 * @date 2024/9/3 22:27
 * <p>
 * commitLog真实数据存储对象模型
 */
public class CommitLogMessageModel {


    /**
     * 真正的消息内容
     */
    private byte[] content;


    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommitLogMessageModel{" +
                "content=" + Arrays.toString(content) +
                '}';
    }

    public byte[] convertToByte() {
        return content;
    }
}

package com.wuyiccc.hellomq.broker.model;

/**
 * @author wuyiccc
 * @date 2024/8/31 15:49
 *
 * commitLog文件的写入对象封装
 */
public class CommitLogModel {

    /**
     * commitLog文件的名称
     */
    private String fileName;

    /**
     * commitLog文件写入的最大上限
     */
    private Long offsetLimit;

    /**
     * 最新commitLog文件写入的地址
     */
    private Long offset;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(Long offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "CommitLogModel{" +
                "fileName='" + fileName + '\'' +
                ", offsetLimit=" + offsetLimit +
                ", offset=" + offset +
                '}';
    }
}

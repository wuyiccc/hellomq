package com.wuyiccc.hellomq.broker.model;

/**
 * @author wuyiccc
 * @date 2024/8/27 23:45
 * <p>
 * 队列模型数据
 */
public class QueueModel {

    private Integer id;

    private String fileName;


    private Integer offsetLimit;

    private Integer latestOffset;

    private Integer lastOffset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getOffsetLimit() {
        return offsetLimit;
    }

    public void setOffsetLimit(Integer offsetLimit) {
        this.offsetLimit = offsetLimit;
    }

    public Integer getLatestOffset() {
        return latestOffset;
    }

    public void setLatestOffset(Integer latestOffset) {
        this.latestOffset = latestOffset;
    }

    public Integer getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(Integer lastOffset) {
        this.lastOffset = lastOffset;
    }

    public int countDiff() {

        return this.offsetLimit - this.latestOffset;
    }
}

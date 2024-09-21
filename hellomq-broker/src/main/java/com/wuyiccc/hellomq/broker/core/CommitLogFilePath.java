package com.wuyiccc.hellomq.broker.core;

/**
 * @author wuyiccc
 * @date 2024/9/21 20:31
 */
public class CommitLogFilePath {

    private String filePath;

    private String fileName;


    public CommitLogFilePath(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

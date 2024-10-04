package com.wuyiccc.hellomq.broker.model;

import com.wuyiccc.hellomq.broker.utils.ByteConvertUtils;

/**
 * @author wuyiccc
 * @date 2024/9/22 12:19
 */
public class ConsumeQueueDetailModel {

    private int commitLogFileName;

    private int msgIndex;


    private int msgLength;


    public int getCommitLogFileName() {
        return commitLogFileName;
    }

    public void setCommitLogFileName(int commitLogFileName) {
        this.commitLogFileName = commitLogFileName;
    }

    public int getMsgIndex() {
        return msgIndex;
    }

    public void setMsgIndex(int msgIndex) {
        this.msgIndex = msgIndex;
    }

    public int getMsgLength() {
        return msgLength;
    }

    public void setMsgLength(int msgLength) {
        this.msgLength = msgLength;
    }

    public byte[] convertToBytes() {

        byte[] commitLogFileNameBytes = ByteConvertUtils.intToBytes(commitLogFileName);
        byte[] msgBytes = ByteConvertUtils.intToBytes(msgIndex);
        byte[] msgLengthBytes = ByteConvertUtils.intToBytes(msgLength);

        byte[] finalBytes = new byte[12];


        int p = 0;
        for (int i = 0; i < 4; i++) {
            finalBytes[p++] = commitLogFileNameBytes[i];
        }
        for (int i = 0; i < 4; i++) {
            finalBytes[p++] = msgBytes[i];
        }
        for (int i = 0; i < 4; i++) {
            finalBytes[p++] = msgLengthBytes[i];
        }

        return finalBytes;
    }

    public void buildFromBytes(byte[] body) {

        // 0~4 int
        this.setCommitLogFileName(ByteConvertUtils.bytesToInt(
                ByteConvertUtils.readInPos(body, 0, 4)
        ));

        // 4~8 int
        this.setMsgIndex(ByteConvertUtils.bytesToInt(
                ByteConvertUtils.readInPos(body, 4, 4)
        ));

        // 8~12 int
        this.setMsgLength(ByteConvertUtils.bytesToInt(
                ByteConvertUtils.readInPos(body, 8, 4)
        ));
    }
}

package com.wuyiccc.hellomq.broker.utils;

/**
 * @author wuyiccc
 * @date 2024/8/31 17:32
 */
public class CommitLogFileNameUtils {

    private static final Integer defaultCommitLogFileNameLen = 8;

    /**
     * 构建第一份commitLog文件名称
     */
    public static String buildLatestCommitLogName() {

        return "0000000";
    }


    /**
     * 根据旧的commitLog文件名生成新的commitLog文件名
     */
    public static String incrCommitLogFileName(String oldFileName) {

        if (oldFileName.length() != defaultCommitLogFileNameLen) {
            throw new IllegalArgumentException("fileName must has " + defaultCommitLogFileNameLen + " chars");
        }

        Long fileIndex = Long.valueOf(oldFileName);

        fileIndex++;

        String newFileName = String.valueOf(fileIndex);

        int currentFileNameLen = newFileName.length();
        int needFullLen = defaultCommitLogFileNameLen - currentFileNameLen;
        if (needFullLen < 0) {
            throw new RuntimeException("unKnow fileName error");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < needFullLen; i++) {
            sb.append("0");
        }

        sb.append(newFileName);
        return sb.toString();
    }

    public static void main(String[] args) {
        String s = incrCommitLogFileName("00000013");
        System.out.println(s);
    }
}

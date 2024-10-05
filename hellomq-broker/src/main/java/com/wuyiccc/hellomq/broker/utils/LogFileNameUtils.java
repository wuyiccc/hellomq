package com.wuyiccc.hellomq.broker.utils;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.common.constants.BrokerConstants;

import java.io.File;

/**
 * @author wuyiccc
 * @date 2024/8/31 17:32
 */
public class LogFileNameUtils {

    private static final Integer defaultCommitLogFileNameLen = 8;

    /**
     * 构建第一份commitLog文件名称
     */
    public static String buildFirstCommitLogName() {

        return BrokerConstants.FIRST_FILE_NAME;
    }


    /**
     * 构建commitLog文件路径
     */
    public static String buildCommitLogFilePath(String topicName, String commitLogFileName) {

        return CommonCache.getGlobalProperties().getHelloMqHome()
                + BrokerConstants.BASE_COMMIT_LOG_PATH
                + topicName
                + File.separator
                + commitLogFileName;
    }

    /**
     * 构建consumeQueue文件路径
     */
    public static String buildConsumeQueueFilePath(String topicName, Integer queueId, String fileName) {

        return CommonCache.getGlobalProperties().getHelloMqHome()
                + BrokerConstants.BASE_CONSUME_QUEUE_PATH
                + topicName
                + File.separator
                + queueId
                + File.separator
                + fileName;
    }


    public static String incrConsumeQueueFileName(String oldFileName) {
        return incrCommitLogFileName(oldFileName);
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

}

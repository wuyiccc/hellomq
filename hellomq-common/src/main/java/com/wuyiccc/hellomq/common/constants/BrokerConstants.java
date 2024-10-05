package com.wuyiccc.hellomq.common.constants;

/**
 * @author wuyiccc
 * @date 2024/8/27 21:34
 */
public class BrokerConstants {

    public static final String HELLO_MQ_HOME_PATH = "HELLO_MQ_HOME_PATH";

    public static final String BASE_COMMIT_LOG_PATH = "/commitlog/";

    public static final String BASE_CONSUME_QUEUE_PATH = "/consumequeue/";

    // 1mb
    public static final Integer COMMITLOG_DEFAULT_MMAP_SIZE = 1 * 1024 * 1024;


    public static final Integer DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP = 3;

    public static final Integer DEFAULT_REFRESH_CONSUME_QUEUE_OFFSET_TIME_STEP = 1;

    private BrokerConstants() {
    }


}

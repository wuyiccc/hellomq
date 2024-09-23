package com.wuyiccc.hellomq.broker.constants;

import java.util.Date;

/**
 * @author wuyiccc
 * @date 2024/8/27 21:34
 */
public class BrokerConstants {

    public static final String HELLO_MQ_HOME_PATH = "HELLO_MQ_HOME_PATH";

    public static final String BASE_STORE_PATH = "/commitlog/";

    // 1mb
    public static final Integer COMMITLOG_DEFAULT_MMAP_SIZE = 1 * 1024 * 1024;


    public static final Integer DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP = 3;

    public static final Integer DEFAULT_REFRESH_CONSUME_QUEUE_OFFSET_TIME_STEP = 1;

    private BrokerConstants() {
    }


}

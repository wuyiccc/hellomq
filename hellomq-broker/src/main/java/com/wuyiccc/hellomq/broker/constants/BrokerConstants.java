package com.wuyiccc.hellomq.broker.constants;

/**
 * @author wuyiccc
 * @date 2024/8/27 21:34
 */
public class BrokerConstants {

    public static final String HELLO_MQ_HOME_PATH = "HELLO_MQ_HOME_PATH";

    public static final String BASE_STORE_PATH = "/broker/store/";

    // 1mb
    public static final Integer COMMITLOG_DEFAULT_MMAP_SIZE = 1 * 1024 * 1024;

    private BrokerConstants() {
    }
}

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

    public static final short DEFAULT_MAGIC_NUM = 17671;


    /**
     * 初始化队列消费进度信息
     */
    public static final String INITIAL_QUEUE_OFFSET = "00000000#0";

    public static final String CONSUME_QUEUE_OFFSET_NAME_SPLIT = "#";

    // 3 * 4int = 12
    public static final int CONSUME_CONTENT_READ_LENGTH = 12;

    public static final String CONSUME_QUEUE_OFFSET_SUB_FILE_NAME = "/config/consumequeue-offset.json";

    public static final String TOPIC_CONFIG_SUB_FILE_NAME = "/config/hellomq-topic.json";

    public static final String FIRST_FILE_NAME = "0000000";

    public static final String NAME_SERVER_CONFIG_FILE_SUB_PATH = "/config/nameserver.properties";

    public static final String BROKER_PROPERTIES_PATH = "/config/broker.properties";

    public static final String PROPERTY_KEY_NAME_SERVER_CONFIG_USER = "nameserver.user";

    public static final String PROPERTY_KEY_NAME_SERVER_CONFIG_PASSWORD = "nameserver.password";

    public static final String PROPERTY_KEY_NAMESERVER_IP = "nameserver.ip";

    public static final String PROPERTY_KEY_NAMESERVER_PORT = "nameserver.port";

    public static final String PROPERTY_KEY_BROKER_PORT = "broker.port";

    private BrokerConstants() {
    }


}

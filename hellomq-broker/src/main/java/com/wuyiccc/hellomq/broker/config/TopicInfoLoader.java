package com.wuyiccc.hellomq.broker.config;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import io.netty.util.internal.StringUtil;

/**
 * @author wuyiccc
 * @date 2024/8/27 21:46
 */
public class TopicInfoLoader {

    private TopicInfo topicInfo;

    public void loadProperties() {

        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getHelloMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("HELLO_MQ_HOME is invalid");
        }
        String topicJsonFilePath = basePath + "/config/broker/hellomq-topic.json";


        topicInfo = new TopicInfo();
    }
}

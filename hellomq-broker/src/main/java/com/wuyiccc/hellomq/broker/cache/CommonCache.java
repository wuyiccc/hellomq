package com.wuyiccc.hellomq.broker.cache;

import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.broker.config.TopicInfo;

/**
 * @author wuyiccc
 * @date 2024/8/27 22:02
 */
public class CommonCache {

    public static GlobalProperties globalProperties = new GlobalProperties();

    public static TopicInfo topicInfo = new TopicInfo();

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public static void setTopicInfo(TopicInfo topicInfo) {
        CommonCache.topicInfo = topicInfo;
    }
}

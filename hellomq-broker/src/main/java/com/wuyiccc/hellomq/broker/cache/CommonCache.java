package com.wuyiccc.hellomq.broker.cache;

import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyiccc
 * @date 2024/8/27 22:02
 */
public class CommonCache {

    private static GlobalProperties globalProperties;


    private static Map<String, MqTopicModel> mqTopicModelMap = new HashMap<>();


    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }


    public static Map<String, MqTopicModel> getMqTopicModelMap() {
        return mqTopicModelMap;
    }

    public static void setMqTopicModelMap(Map<String, MqTopicModel> mqTopicModelMap) {
        CommonCache.mqTopicModelMap = mqTopicModelMap;
    }
}

package com.wuyiccc.hellomq.broker.cache;

import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.broker.model.HelloMqTopicModel;

import java.util.List;

/**
 * @author wuyiccc
 * @date 2024/8/27 22:02
 */
public class CommonCache {

    private static GlobalProperties globalProperties;

    private static List<HelloMqTopicModel> helloMqTopicModelList;

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }

    public static List<HelloMqTopicModel> getHelloMqTopicModelList() {
        return helloMqTopicModelList;
    }

    public static void setHelloMqTopicModelList(List<HelloMqTopicModel> helloMqTopicModelList) {
        CommonCache.helloMqTopicModelList = helloMqTopicModelList;
    }
}

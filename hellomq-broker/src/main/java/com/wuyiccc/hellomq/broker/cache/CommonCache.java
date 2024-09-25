package com.wuyiccc.hellomq.broker.cache;

import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.broker.core.ConsumeQueueMMapFileModelManager;
import com.wuyiccc.hellomq.broker.model.ConsumeQueueOffsetModel;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuyiccc
 * @date 2024/8/27 22:02
 * 全局缓存
 */
public class CommonCache {

    private static GlobalProperties globalProperties;


    private static List<MqTopicModel> mqTopicModelList = new ArrayList<>();

    private static ConsumeQueueOffsetModel consumeQueueDetailModel = new ConsumeQueueOffsetModel();

    private static ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager;

    public static GlobalProperties getGlobalProperties() {
        return globalProperties;
    }

    public static void setGlobalProperties(GlobalProperties globalProperties) {
        CommonCache.globalProperties = globalProperties;
    }


    public static Map<String, MqTopicModel> getMqTopicModelMap() {
        return mqTopicModelList.stream().collect(Collectors.toMap(MqTopicModel::getTopic, item -> item));
    }

    public static void setMqTopicModelList(List<MqTopicModel> mqTopicModelList) {
        CommonCache.mqTopicModelList = mqTopicModelList;
    }

    public static List<MqTopicModel> getMqTopicModelList() {
        return mqTopicModelList;
    }


    public static ConsumeQueueOffsetModel getConsumeQueueDetailModel() {
        return consumeQueueDetailModel;
    }

    public static void setConsumeQueueDetailModel(ConsumeQueueOffsetModel consumeQueueDetailModel) {
        CommonCache.consumeQueueDetailModel = consumeQueueDetailModel;
    }

    public static ConsumeQueueMMapFileModelManager getConsumeQueueMMapFileModelManager() {
        return consumeQueueMMapFileModelManager;
    }

    public static void setConsumeQueueMMapFileModelManager(ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager) {
        CommonCache.consumeQueueMMapFileModelManager = consumeQueueMMapFileModelManager;
    }
}

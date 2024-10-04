package com.wuyiccc.hellomq.broker.cache;

import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.broker.core.CommitLogMMapFileModelManager;
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

    private static ConsumeQueueOffsetModel consumeQueueOffsetModel = new ConsumeQueueOffsetModel();

    private static ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager = new ConsumeQueueMMapFileModelManager();

    private static CommitLogMMapFileModelManager commitLogMMapFileModelManager = new CommitLogMMapFileModelManager();

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


    public static ConsumeQueueOffsetModel getConsumeQueueOffsetModel() {
        return consumeQueueOffsetModel;
    }

    public static void setConsumeQueueOffsetModel(ConsumeQueueOffsetModel consumeQueueOffsetModel) {
        CommonCache.consumeQueueOffsetModel = consumeQueueOffsetModel;
    }

    public static ConsumeQueueMMapFileModelManager getConsumeQueueMMapFileModelManager() {
        return consumeQueueMMapFileModelManager;
    }

    public static void setConsumeQueueMMapFileModelManager(ConsumeQueueMMapFileModelManager consumeQueueMMapFileModelManager) {
        CommonCache.consumeQueueMMapFileModelManager = consumeQueueMMapFileModelManager;
    }

    public static CommitLogMMapFileModelManager getCommitLogMMapFileModelManager() {
        return commitLogMMapFileModelManager;
    }

    public static void setCommitLogMMapFileModelManager(CommitLogMMapFileModelManager commitLogMMapFileModelManager) {
        CommonCache.commitLogMMapFileModelManager = commitLogMMapFileModelManager;
    }
}

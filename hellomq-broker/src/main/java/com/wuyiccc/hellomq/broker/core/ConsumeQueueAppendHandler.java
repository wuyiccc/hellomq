package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;
import com.wuyiccc.hellomq.broker.model.QueueModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuyiccc
 * @date 2024/8/25 21:44
 */
public class ConsumeQueueAppendHandler {



    public void prepareMMapLoading(String topicName) throws IOException {


        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(topicName);
        List<QueueModel> queueList = mqTopicModel.getQueueList();

        List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModelList = new ArrayList<>();
        for (QueueModel queueModel : queueList) {

            ConsumeQueueMMapFileModel consumeQueueMMapFileModel = new ConsumeQueueMMapFileModel();
            consumeQueueMMapFileModel.loadFileInMMap(topicName
                    , queueModel.getId()
                    , queueModel.getLastOffset()
                    , queueModel.getLatestOffset().get()
                    , queueModel.getOffsetLimit());
            consumeQueueMMapFileModelList.add(consumeQueueMMapFileModel);
        }

        CommonCache.getConsumeQueueMMapFileModelManager().put(topicName, consumeQueueMMapFileModelList);
    }


}


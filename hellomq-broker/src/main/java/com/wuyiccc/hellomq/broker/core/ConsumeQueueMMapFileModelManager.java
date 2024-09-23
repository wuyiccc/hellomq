package com.wuyiccc.hellomq.broker.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuyiccc
 * @date 2024/9/23 21:41
 * <p>
 * consumeQueue的mmap映射对象的管理器
 */
public class ConsumeQueueMMapFileModelManager {

    private Map<String, List<ConsumeQueueMMapFileModel>> consumeQueueMMapFileModel = new HashMap<>();


    public void put(String topic, List<ConsumeQueueMMapFileModel> consumeQueueMMapFileModelList) {
        consumeQueueMMapFileModel.put(topic, consumeQueueMMapFileModelList);
    }

    public List<ConsumeQueueMMapFileModel> get(String topic) {
        return consumeQueueMMapFileModel.get(topic);
    }
}

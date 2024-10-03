package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.model.ConsumeQueueOffsetModel;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;
import com.wuyiccc.hellomq.broker.model.QueueModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuyiccc
 * @date 2024/10/3 17:10
 * 消费队列消费处理器
 */
public class ConsumeQueueConsumeHandler {

    /**
     * 读取当前最新一条consumeQueue的消息内容
     */
    public byte[] consume(String topic, String consumeGroup, Integer queueId) {

        // 检查参数合法性
        MqTopicModel mqTopicModel = CommonCache.getMqTopicModelMap().get(topic);
        if (mqTopicModel == null) {
            throw new RuntimeException("topic " + topic + "not exist!");
        }
        // 获取当前匹配的对了最新的consumeQueue的offset是多少
        ConsumeQueueOffsetModel.OffsetTable offsetTable = CommonCache.getConsumeQueueOffsetModel().getOffsetTable();
        Map<String, ConsumeQueueOffsetModel.ConsumeGroupDetail> topicConsumerGroupDetailMap = offsetTable.getTopicConsumerGroupDetailMap();
        ConsumeQueueOffsetModel.ConsumeGroupDetail consumeGroupDetail = topicConsumerGroupDetailMap.get(topic);
        // 如果是首次消费
        if (consumeGroupDetail == null) {
            consumeGroupDetail = new ConsumeQueueOffsetModel.ConsumeGroupDetail();
            topicConsumerGroupDetailMap.put(topic, consumeGroupDetail);
        }
        Map<String, Map<String, String>> consumerGroupDetailMap = consumeGroupDetail.getConsumerGroupDetailMap();
        Map<String, String> queueOffsetDetailMap = consumerGroupDetailMap.get(consumeGroup);
        if (queueOffsetDetailMap == null) {
            queueOffsetDetailMap = new HashMap<>();
            List<QueueModel> queueList = mqTopicModel.getQueueList();
            for (QueueModel queueModel : queueList) {
                queueOffsetDetailMap.put(String.valueOf(queueModel.getId()), "00000000#0");
            }
            consumerGroupDetailMap.put(consumeGroup, queueOffsetDetailMap);
        }

        String offsetStrInfo = queueOffsetDetailMap.get(String.valueOf(queueId));
        String[] offsetStrArr = offsetStrInfo.split("#");
        String consumeQueueFileName = offsetStrArr[0];
        Integer consumeQueueOffset = Integer.valueOf(offsetStrArr[1]);

        // 获取当前匹配队列的mmap对象
        return null;
    }


    /**
     * 更新consumeQueue-offset的值
     */
    public boolean ack() {

        return true;
    }
}

package com.wuyiccc.hellomq.broker.model;

import java.util.Map;

/**
 * @author wuyiccc
 * @date 2024/9/23 20:46
 */
public class ConsumeQueueOffsetModel {

    private OffsetTable offsetTable;


    public OffsetTable getOffsetTable() {
        return offsetTable;
    }

    public void setOffsetTable(OffsetTable offsetTable) {
        this.offsetTable = offsetTable;
    }

    public static class OffsetTable {

        private Map<String, ConsumeGroupDetail> topicConsumerGroupDetailMap;

        public Map<String, ConsumeGroupDetail> getTopicConsumerGroupDetailMap() {
            return topicConsumerGroupDetailMap;
        }

        public void setTopicConsumerGroupDetailMap(Map<String, ConsumeGroupDetail> topicConsumerGroupDetailMap) {
            this.topicConsumerGroupDetailMap = topicConsumerGroupDetailMap;
        }
    }

    public static class ConsumeGroupDetail {

        private Map<String, Map<String, String>> consumerGroupDetailMap;

        public Map<String, Map<String, String>> getConsumerGroupDetailMap() {
            return consumerGroupDetailMap;
        }

        public void setConsumerGroupDetailMap(Map<String, Map<String, String>> consumerGroupDetailMap) {
            this.consumerGroupDetailMap = consumerGroupDetailMap;
        }
    }

}

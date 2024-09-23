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

    private class OffsetTable {

        private Map<String, ConsumeGroupDetail> topicConsumerGroupDetail;

        public Map<String, ConsumeGroupDetail> getTopicConsumerGroupDetail() {
            return topicConsumerGroupDetail;
        }

        public void setTopicConsumerGroupDetail(Map<String, ConsumeGroupDetail> topicConsumerGroupDetail) {
            this.topicConsumerGroupDetail = topicConsumerGroupDetail;
        }
    }

    private class ConsumeGroupDetail {

        private Map<String, Map<String, String>> consumerGroupDetailMap;

        public Map<String, Map<String, String>> getConsumerGroupDetailMap() {
            return consumerGroupDetailMap;
        }

        public void setConsumerGroupDetailMap(Map<String, Map<String, String>> consumerGroupDetailMap) {
            this.consumerGroupDetailMap = consumerGroupDetailMap;
        }
    }

}

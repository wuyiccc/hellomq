package com.wuyiccc.hellomq.broker.model;

import java.util.List;

/**
 * @author wuyiccc
 * @date 2024/8/27 23:45
 *
 * mq的topic映射对象
 */
public class HelloMqTopicModel {

    private String topic;

    private List<QueueModel> queueList;

    private String createAt;

    private String updateAt;


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<QueueModel> getQueueList() {
        return queueList;
    }

    public void setQueueList(List<QueueModel> queueList) {
        this.queueList = queueList;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "HelloMqTopicModel{" +
                "topic='" + topic + '\'' +
                ", queueList=" + queueList +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                '}';
    }
}



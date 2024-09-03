package com.wuyiccc.hellomq.broker;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.core.CommitLogAppendHandler;
import com.wuyiccc.hellomq.broker.loader.GlobalPropertiesLoader;
import com.wuyiccc.hellomq.broker.loader.MqTopicLoader;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * @author wuyiccc
 * @date 2024/8/27 23:55
 */
public class BrokerStartUp {


    private static GlobalPropertiesLoader globalPropertiesLoader;

    private static MqTopicLoader mqTopicLoader;

    private static CommitLogAppendHandler commitLogAppendHandler;


    private static void initProperties() throws IOException {

        globalPropertiesLoader = new GlobalPropertiesLoader();
        // 加载全局配置文件
        globalPropertiesLoader.loadProperties();
        mqTopicLoader = new MqTopicLoader();
        // 加载topic信息
        mqTopicLoader.loadProperties();
        commitLogAppendHandler = new CommitLogAppendHandler();

        Collection<MqTopicModel> mqTopicModelList = CommonCache.getMqTopicModelMap().values();

        for (MqTopicModel mqTopicModel : mqTopicModelList) {
            String topicName = mqTopicModel.getTopic();
            commitLogAppendHandler.prepareMMapLoading(topicName);
        }
    }

    public static void main(String[] args) throws IOException {

        initProperties();

        String topic = "test_topic";
        commitLogAppendHandler.appendMsg(topic, "this is a test content".getBytes(StandardCharsets.UTF_8));
        commitLogAppendHandler.readMsg(topic);
    }
}

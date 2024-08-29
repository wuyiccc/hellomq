package com.wuyiccc.hellomq.broker;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.constants.BrokerConstants;
import com.wuyiccc.hellomq.broker.core.CommitLogAppendHandler;
import com.wuyiccc.hellomq.broker.loader.GlobalPropertiesLoader;
import com.wuyiccc.hellomq.broker.loader.HelloMqTopicLoader;
import com.wuyiccc.hellomq.broker.model.HelloMqTopicModel;

import java.io.IOException;

/**
 * @author wuyiccc
 * @date 2024/8/27 23:55
 */
public class BrokerStartUp {


    private static GlobalPropertiesLoader globalPropertiesLoader;

    private static HelloMqTopicLoader helloMqTopicLoader;

    private static CommitLogAppendHandler commitLogAppendHandler;


    private static void initProperties() throws IOException {

        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        helloMqTopicLoader = new HelloMqTopicLoader();
        helloMqTopicLoader.loadProperties();
        commitLogAppendHandler = new CommitLogAppendHandler();

        for (HelloMqTopicModel helloMqTopicModel : CommonCache.getHelloMqTopicModelList()) {
            String topicName = helloMqTopicModel.getTopic();
            String filePath = CommonCache.getGlobalProperties().getHelloMqHome()
                    + BrokerConstants.BASE_STORE_PATH
                    + topicName
                    + "/00000000";
            commitLogAppendHandler.prepareMMapLoading(filePath, topicName);
        }
    }

    public static void main(String[] args) throws IOException {

        initProperties();

        String topic = "test_topic";
        commitLogAppendHandler.appendMsg(topic, "this is a test content");
        commitLogAppendHandler.readMsg(topic);
    }
}

package com.wuyiccc.hellomq.broker;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.constants.BrokerConstants;
import com.wuyiccc.hellomq.broker.core.MessageAppenderHandler;
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

    private static MessageAppenderHandler messageAppenderHandler;


    private static void initProperties() throws IOException {

        globalPropertiesLoader = new GlobalPropertiesLoader();
        globalPropertiesLoader.loadProperties();
        helloMqTopicLoader = new HelloMqTopicLoader();
        helloMqTopicLoader.loadProperties();
        messageAppenderHandler = new MessageAppenderHandler();

        for (HelloMqTopicModel helloMqTopicModel : CommonCache.getHelloMqTopicModelList()) {
            String topicName = helloMqTopicModel.getTopic();
            String filePath = CommonCache.getGlobalProperties().getHelloMqHome()
                    + BrokerConstants.BASE_STORE_PATH
                    + topicName
                    + "/00000000";
            messageAppenderHandler.prepareMMapLoading(filePath, topicName);
        }
    }

    public static void main(String[] args) throws IOException {

        initProperties();

        String topic = "test_topic";
        messageAppenderHandler.appendMsg(topic, "this is a test content");
        messageAppenderHandler.readMsg(topic);
    }
}

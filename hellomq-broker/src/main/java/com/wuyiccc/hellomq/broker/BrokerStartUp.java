package com.wuyiccc.hellomq.broker;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.core.CommitLogAppendHandler;
import com.wuyiccc.hellomq.broker.core.ConsumeQueueAppendHandler;
import com.wuyiccc.hellomq.broker.core.ConsumeQueueConsumeHandler;
import com.wuyiccc.hellomq.broker.loader.ConsumeQueueOffsetLoader;
import com.wuyiccc.hellomq.broker.loader.GlobalPropertiesLoader;
import com.wuyiccc.hellomq.broker.loader.MqTopicLoader;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author wuyiccc
 * @date 2024/8/27 23:55
 */
public class BrokerStartUp {


    private static GlobalPropertiesLoader globalPropertiesLoader;

    private static MqTopicLoader mqTopicLoader;

    private static CommitLogAppendHandler commitLogAppendHandler;

    private static ConsumeQueueOffsetLoader consumeQueueOffsetLoader;

    private static ConsumeQueueAppendHandler consumeQueueAppendHandler;

    private static ConsumeQueueConsumeHandler consumeQueueConsumeHandler;

    private static void initProperties() throws IOException {

        globalPropertiesLoader = new GlobalPropertiesLoader();
        mqTopicLoader = new MqTopicLoader();
        consumeQueueOffsetLoader = new ConsumeQueueOffsetLoader();
        consumeQueueConsumeHandler = new ConsumeQueueConsumeHandler();
        commitLogAppendHandler = new CommitLogAppendHandler();
        consumeQueueAppendHandler = new ConsumeQueueAppendHandler();

        // 加载全局配置文件
        globalPropertiesLoader.loadProperties();

        // 加载topic信息
        mqTopicLoader.loadProperties();
        mqTopicLoader.startRefreshMqTopicInfoTask();

        consumeQueueOffsetLoader.loadProperties();
        consumeQueueOffsetLoader.startRefreshConsumeQueueInfoTask();

        Collection<MqTopicModel> mqTopicModelList = CommonCache.getMqTopicModelMap().values();

        for (MqTopicModel mqTopicModel : mqTopicModelList) {
            String topicName = mqTopicModel.getTopic();
            commitLogAppendHandler.prepareMMapLoading(topicName);
            consumeQueueAppendHandler.prepareMMapLoading(topicName);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        initProperties();

        String topic = "test_topic";
        String consumerGroup = "test_service_group";


        for (int i = 0; i < 50000; i++) {
            //commitLogAppendHandler.appendMsg(topic, ("this is content" + i).getBytes(StandardCharsets.UTF_8));
            //System.out.println("写入数据");

            consumeQueueConsumeHandler.consume(topic, consumerGroup, 0);
            TimeUnit.MILLISECONDS.sleep(100);
        }
        //commitLogAppendHandler.readMsg(topic);
    }
}

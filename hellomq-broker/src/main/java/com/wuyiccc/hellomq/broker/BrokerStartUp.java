package com.wuyiccc.hellomq.broker;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.core.CommitLogAppendHandler;
import com.wuyiccc.hellomq.broker.core.ConsumeQueueAppendHandler;
import com.wuyiccc.hellomq.broker.core.ConsumeQueueConsumeHandler;
import com.wuyiccc.hellomq.broker.loader.ConsumeQueueOffsetLoader;
import com.wuyiccc.hellomq.broker.loader.GlobalPropertiesLoader;
import com.wuyiccc.hellomq.broker.loader.MqTopicLoader;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuyiccc
 * @date 2024/8/27 23:55
 */
public class BrokerStartUp {


    private static final Logger log = LoggerFactory.getLogger(BrokerStartUp.class);

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
        String userServiceConsumerGroup = "user_service_group";
        String orderServiceConsumeGroup = "order_service_group";

        new Thread(() -> {
            while (true) {
                byte[] content = consumeQueueConsumeHandler.consume(topic, userServiceConsumerGroup, 0);
                if (content != null && content.length != 0) {
                    log.info(userServiceConsumerGroup + ", 消费内容: " + new String(content));
                    consumeQueueConsumeHandler.ack(topic, userServiceConsumerGroup, 0);
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                byte[] content = consumeQueueConsumeHandler.consume(topic, orderServiceConsumeGroup, 0);
                if (content != null && content.length != 0) {
                    log.info(orderServiceConsumeGroup + ", 消费内容: " + new String(content));
                    consumeQueueConsumeHandler.ack(topic, orderServiceConsumeGroup, 0);
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

        AtomicInteger i = new AtomicInteger();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                try {
                    commitLogAppendHandler.appendMsg(topic, ("message_" + (i.getAndIncrement())).getBytes(StandardCharsets.UTF_8));
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }


        }).start();

    }
}

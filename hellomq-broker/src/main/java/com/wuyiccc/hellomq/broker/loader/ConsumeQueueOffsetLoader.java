package com.wuyiccc.hellomq.broker.loader;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.config.CommonThreadPoolConfig;
import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import com.wuyiccc.hellomq.broker.model.ConsumeQueueOffsetModel;
import com.wuyiccc.hellomq.broker.utils.FileContentUtils;
import com.wuyiccc.hellomq.broker.utils.JsonUtils;
import io.netty.util.internal.StringUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author wuyiccc
 * @date 2024/9/23 21:02
 */
public class ConsumeQueueOffsetLoader {

    private String filePath;

    public void loadProperties() {

        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getHelloMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("HELLO_MQ_HOME is invalid");
        }
        filePath = basePath + "/config/consumequeue-offset.json";

        String fileContent = FileContentUtils.readFromFile(filePath);

        ConsumeQueueOffsetModel mqTopicModelList = JsonUtils.jsonToPojo(fileContent, ConsumeQueueOffsetModel.class);

        CommonCache.setConsumeQueueOffsetModel(mqTopicModelList);
    }


    public void startRefreshConsumeQueueInfoTask() {


        // 异步线程
        // 每间隔10s将内存中的配置刷新到磁盘里面
        CommonThreadPoolConfig.refreshConsumeQueueOffsetExecutor.execute(new Runnable() {
            @Override
            public void run() {

                do {
                    try {
                        TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_CONSUME_QUEUE_OFFSET_TIME_STEP);

                        System.out.println("consume-queue-offset 刷盘中");
                        // 刷盘
                        ConsumeQueueOffsetModel consumeQueueDetailModel = CommonCache.getConsumeQueueOffsetModel();
                        FileContentUtils.overWriteToFile(filePath, JsonUtils.objectToJson(consumeQueueDetailModel, true));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } while (true);
            }
        });
    }
}

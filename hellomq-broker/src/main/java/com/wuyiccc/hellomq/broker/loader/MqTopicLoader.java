package com.wuyiccc.hellomq.broker.loader;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.config.CommonThreadPoolConfig;
import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import com.wuyiccc.hellomq.broker.model.MqTopicModel;
import com.wuyiccc.hellomq.broker.utils.FileContentUtils;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wuyiccc
 * @date 2024/8/27 21:46
 */
public class MqTopicLoader {

    private static final Logger log = LoggerFactory.getLogger(MqTopicLoader.class);

    private String filePath;

    public void loadProperties() {

        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getHelloMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("HELLO_MQ_HOME is invalid");
        }
        filePath = basePath + BrokerConstants.TOPIC_CONFIG_SUB_FILE_NAME;

        String fileContent = FileContentUtils.readFromFile(filePath);

        List<MqTopicModel> mqTopicModelList = JsonUtils.toList(fileContent, MqTopicModel.class);

        CommonCache.setMqTopicModelList(mqTopicModelList);
    }

    public void startRefreshMqTopicInfoTask() {


        // 异步线程
        // 每间隔10s将内存中的配置刷新到磁盘里面
        CommonThreadPoolConfig.refreshHelloMqTopicExecutor.execute(() -> {

            do {
                try {
                    TimeUnit.SECONDS.sleep(BrokerConstants.DEFAULT_REFRESH_MQ_TOPIC_TIME_STEP);

                    log.info("commitlog刷盘中");
                    // 刷盘
                    List<MqTopicModel> topicModelList = CommonCache.getMqTopicModelList();
                    FileContentUtils.overWriteToFile(filePath, JsonUtils.toJsonStr(topicModelList, true));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } while (true);
        });
    }
}

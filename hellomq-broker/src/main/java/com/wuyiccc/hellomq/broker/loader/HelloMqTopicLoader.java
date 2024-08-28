package com.wuyiccc.hellomq.broker.loader;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.broker.model.HelloMqTopicModel;
import com.wuyiccc.hellomq.broker.utils.FileContentReaderUtils;
import com.wuyiccc.hellomq.broker.utils.JsonUtils;
import io.netty.util.internal.StringUtil;

import java.util.List;

/**
 * @author wuyiccc
 * @date 2024/8/27 21:46
 */
public class HelloMqTopicLoader {

    public void loadProperties() {

        GlobalProperties globalProperties = CommonCache.getGlobalProperties();
        String basePath = globalProperties.getHelloMqHome();
        if (StringUtil.isNullOrEmpty(basePath)) {
            throw new IllegalArgumentException("HELLO_MQ_HOME is invalid");
        }
        String topicJsonFilePath = basePath + "/config/hellomq-topic.json";

        String fileContent = FileContentReaderUtils.readFromFile(topicJsonFilePath);

        List<HelloMqTopicModel> helloMqTopicLoaderList = JsonUtils.jsonToList(fileContent, HelloMqTopicModel.class);

        CommonCache.setHelloMqTopicModelList(helloMqTopicLoaderList);
    }
}

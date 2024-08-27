package com.wuyiccc.hellomq.broker.config;

import com.sun.xml.internal.ws.util.StringUtils;
import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.constants.BrokerConstants;
import io.netty.util.internal.StringUtil;

/**
 * @author wuyiccc
 * @date 2024/8/27 21:32
 */
public class GlobalPropertiesLoader {

    private GlobalProperties globalProperties;

    public void loadProperties() {

        globalProperties = new GlobalProperties();

        String helloMqHome = System.getenv(BrokerConstants.HELLO_MQ_HOME_PATH);
        if (StringUtil.isNullOrEmpty(helloMqHome)) {
            throw new IllegalArgumentException("HELLO_MQ_HOME_PATH is null or empty");
        }

        globalProperties.setHelloMqHome(System.getProperty(BrokerConstants.HELLO_MQ_HOME_PATH));

        CommonCache.globalProperties = globalProperties;
    }


}

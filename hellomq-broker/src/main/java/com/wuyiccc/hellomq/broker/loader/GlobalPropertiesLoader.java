package com.wuyiccc.hellomq.broker.loader;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import io.netty.util.internal.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

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

        globalProperties.setHelloMqHome(helloMqHome);

        Properties properties = new Properties();
        try {

            properties.load(new FileInputStream(new File(helloMqHome + BrokerConstants.BROKER_PROPERTIES_PATH)));
            globalProperties.setNameserverIp(properties.getProperty(BrokerConstants.PROPERTY_KEY_NAMESERVER_IP));
            globalProperties.setNameserverPort(Integer.valueOf(properties.getProperty(BrokerConstants.PROPERTY_KEY_NAMESERVER_PORT)));
            globalProperties.setNameserverUser(properties.getProperty(BrokerConstants.PROPERTY_KEY_NAME_SERVER_CONFIG_USER));
            globalProperties.setNameserverPassword(properties.getProperty(BrokerConstants.PROPERTY_KEY_NAME_SERVER_CONFIG_PASSWORD));
            globalProperties.setBrokerPort(Integer.valueOf(properties.getProperty(BrokerConstants.PROPERTY_KEY_BROKER_PORT)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        CommonCache.setGlobalProperties(globalProperties);
    }


}

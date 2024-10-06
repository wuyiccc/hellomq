package com.wuyiccc.hellomq.nameserver.core;

import com.wuyiccc.hellomq.common.constants.BrokerConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author wuyiccc
 * @date 2024/10/6 10:33
 */
public class PropertiesLoader {

    private Properties properties = new Properties();


    public void loadProperties() throws IOException {

        String helloMqHome = System.getenv(BrokerConstants.HELLO_MQ_HOME_PATH);
        properties.load(new FileInputStream(new File(helloMqHome + BrokerConstants.NAME_SERVER_CONFIG_FILE_SUB_PATH)));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}

package com.wuyiccc.hellomq.nameserver.core;

import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import com.wuyiccc.hellomq.common.constants.StrConstants;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.config.MasterSlavingReplicationProperties;
import com.wuyiccc.hellomq.nameserver.config.NameServerProperties;
import com.wuyiccc.hellomq.nameserver.config.TraceReplicationProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * @author wuyiccc
 * @date 2024/10/6 10:33
 */
public class PropertiesLoader {

    private static final Logger log = LoggerFactory.getLogger(PropertiesLoader.class);


    private Properties properties = new Properties();


    public void loadProperties() throws IOException {

        String helloMqHome = System.getenv(BrokerConstants.HELLO_MQ_HOME_PATH);
        properties.load(Files.newInputStream(new File(helloMqHome + BrokerConstants.NAME_SERVER_CONFIG_FILE_SUB_PATH).toPath()));
        NameServerProperties nameServerProperties = new NameServerProperties();
        nameServerProperties.setNameserverPwd(getStr(BrokerConstants.PROPERTY_KEY_NAME_SERVER_CONFIG_PASSWORD));
        nameServerProperties.setNameserverUser(getStr(BrokerConstants.PROPERTY_KEY_NAME_SERVER_CONFIG_USER));
        nameServerProperties.setNameserverPort(getInt(BrokerConstants.PROPERTY_KEY_NAMESERVER_PORT));
        nameServerProperties.setReplicationMode(getStr(BrokerConstants.PROPERTY_KEY_NAMESERVER_REPLICATION_MODE));

        TraceReplicationProperties traceReplicationProperties = new TraceReplicationProperties();
        traceReplicationProperties.setNextNode(getStrCanBeNull(BrokerConstants.PROPERTY_KEY_NAMESERVER_REPLICATION_TRACE_NEXT_NODE));
        traceReplicationProperties.setPort(getIntCanBeNull(BrokerConstants.PROPERTY_KEY_NAMESERVER_REPLICATION_TRACE_PORT));
        nameServerProperties.setTraceReplicationProperties(traceReplicationProperties);

        MasterSlavingReplicationProperties masterSlavingReplicationProperties = new MasterSlavingReplicationProperties();
        masterSlavingReplicationProperties.setMaster(getStrCanBeNull(BrokerConstants.PROPERTY_KEY_NAMESERVER_REPLICATION_MASTER));
        masterSlavingReplicationProperties.setRole(getStrCanBeNull(BrokerConstants.PROPERTY_KEY_NAMESERVER_REPLICATION_MASTER_SLAVE_ROLE));
        masterSlavingReplicationProperties.setType(getStrCanBeNull(BrokerConstants.PROPERTY_KEY_NAMESERVER_REPLICATION_MASTER_SLAVE_TYPE));
        masterSlavingReplicationProperties.setPort(getInt(BrokerConstants.PROPERTY_KEY_NAMESERVER_REPLICATION_PORT));

        nameServerProperties.setMasterSlavingReplicationProperties(masterSlavingReplicationProperties);


        log.info("nameServerProperties: {}", JsonUtils.toJsonStr(nameServerProperties, true));
        CommonCache.setNameServerProperties(nameServerProperties);
    }


    private String getStrCanBeNull(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            return null;
        }
        return value;
    }

    private String getStr(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("配置参数: " + key + "不存在");
        }
        return value;
    }

    private Integer getIntCanBeNull(String key) {

        String value = properties.getProperty(key);
        if (value == null) {
            return null;
        }

        if (StringUtils.isBlank(value)) {
            return null;
        }

        return Integer.valueOf(value);
    }

    private Integer getInt(String key) {
        return Integer.valueOf(getStr(key));
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}

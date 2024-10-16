package com.wuyiccc.hellomq.nameserver.cache;

import com.wuyiccc.hellomq.nameserver.config.NameServerProperties;
import com.wuyiccc.hellomq.nameserver.core.PropertiesLoader;
import com.wuyiccc.hellomq.nameserver.replication.MasterReplicationMsgSendTask;
import com.wuyiccc.hellomq.nameserver.store.ReplicationChannelManager;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstanceManager;

/**
 * @author wuyiccc
 * @date 2024/10/6 10:38
 */
public class CommonCache {

    private static PropertiesLoader propertiesLoader = new PropertiesLoader();


    private static ServiceInstanceManager serviceInstanceManager = new ServiceInstanceManager();


    private static NameServerProperties nameServerProperties = new NameServerProperties();

    private static ReplicationChannelManager replicationChannelManager = new ReplicationChannelManager();

    private static MasterReplicationMsgSendTask masterReplicationMsgSendTask = new MasterReplicationMsgSendTask();

    public static PropertiesLoader getPropertiesLoader() {
        return propertiesLoader;
    }

    public static void setPropertiesLoader(PropertiesLoader propertiesLoader) {
        CommonCache.propertiesLoader = propertiesLoader;
    }

    public static ServiceInstanceManager getServiceInstanceManager() {
        return serviceInstanceManager;
    }

    public static void setServiceInstanceManager(ServiceInstanceManager serviceInstanceManager) {
        CommonCache.serviceInstanceManager = serviceInstanceManager;
    }

    public static NameServerProperties getNameServerProperties() {
        return nameServerProperties;
    }

    public static void setNameServerProperties(NameServerProperties nameServerProperties) {
        CommonCache.nameServerProperties = nameServerProperties;
    }

    public static ReplicationChannelManager getReplicationChannelManager() {
        return replicationChannelManager;
    }

    public static void setReplicationChannelManager(ReplicationChannelManager replicationChannelManager) {
        CommonCache.replicationChannelManager = replicationChannelManager;
    }

    public static MasterReplicationMsgSendTask getMasterReplicationMsgSendTask() {
        return masterReplicationMsgSendTask;
    }

    public static void setMasterReplicationMsgSendTask(MasterReplicationMsgSendTask masterReplicationMsgSendTask) {
        CommonCache.masterReplicationMsgSendTask = masterReplicationMsgSendTask;
    }
}

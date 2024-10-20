package com.wuyiccc.hellomq.nameserver.cache;

import com.wuyiccc.hellomq.common.dto.SlaveAckDTO;
import com.wuyiccc.hellomq.nameserver.config.NameServerProperties;
import com.wuyiccc.hellomq.nameserver.core.PropertiesLoader;
import com.wuyiccc.hellomq.nameserver.replication.ReplicationTask;
import com.wuyiccc.hellomq.nameserver.store.ReplicationChannelManager;
import com.wuyiccc.hellomq.nameserver.store.ReplicationMsgQueueManager;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstanceManager;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author wuyiccc
 * @date 2024/10/6 10:38
 */
public class CommonCache {

    private static PropertiesLoader propertiesLoader = new PropertiesLoader();


    private static ServiceInstanceManager serviceInstanceManager = new ServiceInstanceManager();


    private static NameServerProperties nameServerProperties = new NameServerProperties();

    private static ReplicationChannelManager replicationChannelManager = new ReplicationChannelManager();

    private static ReplicationTask replicationTask;

    private static Channel masterConnection = null;

    private static ReplicationMsgQueueManager replicationMsgQueueManager = new ReplicationMsgQueueManager();

    private static Map<String, SlaveAckDTO> ackMap = new ConcurrentHashMap<>();

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


    public static ReplicationTask getReplicationTask() {
        return replicationTask;
    }

    public static void setReplicationTask(ReplicationTask replicationTask) {
        CommonCache.replicationTask = replicationTask;
    }

    public static Channel getMasterConnection() {
        return masterConnection;
    }

    public static void setMasterConnection(Channel masterConnection) {
        CommonCache.masterConnection = masterConnection;
    }

    public static ReplicationMsgQueueManager getReplicationMsgQueueManager() {
        return replicationMsgQueueManager;
    }

    public static void setReplicationMsgQueueManager(ReplicationMsgQueueManager replicationMsgQueueManager) {
        CommonCache.replicationMsgQueueManager = replicationMsgQueueManager;
    }

    public static Map<String, SlaveAckDTO> getAckMap() {
        return ackMap;
    }

    public static void setAckMap(Map<String, SlaveAckDTO> ackMap) {
        CommonCache.ackMap = ackMap;
    }
}

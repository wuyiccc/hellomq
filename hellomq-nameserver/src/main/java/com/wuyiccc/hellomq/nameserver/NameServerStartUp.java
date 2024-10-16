package com.wuyiccc.hellomq.nameserver;

import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.core.InValidServiceRemoveTask;
import com.wuyiccc.hellomq.nameserver.core.NameServerStarter;
import com.wuyiccc.hellomq.nameserver.enums.ReplicationModeEnum;
import com.wuyiccc.hellomq.nameserver.replication.ReplicationService;

import java.io.IOException;

/**
 * @author wuyiccc
 * @date 2024/10/5 16:17
 */
public class NameServerStartUp {

    private static NameServerStarter nameServerStarter;

    private static ReplicationService replicationService = new ReplicationService();

    private static void initReplication() {

        ReplicationModeEnum replicationModeEnum = replicationService.checkProperties();
        replicationService.startReplicationTask(replicationModeEnum);
        if (replicationModeEnum == ReplicationModeEnum.MASTER_SLAVE) {
            CommonCache.getMasterReplicationMsgSendTask().startSendReplicationMsgTask();
        }
    }

    private static void initInvalidServerRemoveTask() {

        Thread inValidServiceRemoveTask = new Thread(new InValidServiceRemoveTask());
        inValidServiceRemoveTask.setName("invalid-server-remove-task");
        inValidServiceRemoveTask.start();
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        CommonCache.getPropertiesLoader().loadProperties();
        initReplication();
        initInvalidServerRemoveTask();
        nameServerStarter = new NameServerStarter(CommonCache.getNameServerProperties().getNameserverPort());
        nameServerStarter.startServer();
    }

}

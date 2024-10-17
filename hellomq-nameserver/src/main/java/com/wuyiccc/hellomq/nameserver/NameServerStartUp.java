package com.wuyiccc.hellomq.nameserver;

import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.core.InValidServiceRemoveTask;
import com.wuyiccc.hellomq.nameserver.core.NameServerStarter;
import com.wuyiccc.hellomq.nameserver.enums.ReplicationModeEnum;
import com.wuyiccc.hellomq.nameserver.enums.ReplicationRoleEnum;
import com.wuyiccc.hellomq.nameserver.replication.MasterReplicationMsgSendTask;
import com.wuyiccc.hellomq.nameserver.replication.ReplicationService;
import com.wuyiccc.hellomq.nameserver.replication.ReplicationTask;
import com.wuyiccc.hellomq.nameserver.replication.SlaveReplicationHeartBeatTask;

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
            String role = CommonCache.getNameServerProperties().getMasterSlavingReplicationProperties().getRole();
            ReplicationRoleEnum roleEnum = ReplicationRoleEnum.of(role);
            ReplicationTask replicationTask = null;

            if (roleEnum == ReplicationRoleEnum.MASTER) {
                replicationTask = new MasterReplicationMsgSendTask("master-replication-msg-send-task");
                replicationTask.startTaskAsync();
            } else if (roleEnum == ReplicationRoleEnum.SLAVE) {
                replicationTask = new SlaveReplicationHeartBeatTask("slave-replication-heart-beat-send-task");
                replicationTask.startTaskAsync();
            }

            CommonCache.setReplicationTask(replicationTask);
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

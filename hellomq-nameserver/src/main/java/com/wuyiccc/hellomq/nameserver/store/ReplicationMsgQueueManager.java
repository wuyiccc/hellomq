package com.wuyiccc.hellomq.nameserver.store;

import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.enums.ReplicationModeEnum;
import com.wuyiccc.hellomq.nameserver.enums.ReplicationRoleEnum;
import com.wuyiccc.hellomq.nameserver.event.model.ReplicationMsgEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author wuyiccc
 * @date 2024/10/19 16:48
 */
public class ReplicationMsgQueueManager {

    private BlockingQueue<ReplicationMsgEvent> replicationMsgEventQueue = new ArrayBlockingQueue<>(5000);

    public BlockingQueue<ReplicationMsgEvent> getReplicationMsgEventQueue() {
        return replicationMsgEventQueue;
    }

    public void put(ReplicationMsgEvent replicationMsgEvent) {

        ReplicationModeEnum replicationModeEnum = ReplicationModeEnum.of(CommonCache.getNameServerProperties().getReplicationMode());

        if (replicationModeEnum == null) {

            return;
        }

        if (replicationModeEnum == ReplicationModeEnum.MASTER_SLAVE) {
            ReplicationRoleEnum roleEnum = ReplicationRoleEnum.of(CommonCache.getNameServerProperties().getMasterSlavingReplicationProperties().getRole());
            if (roleEnum != ReplicationRoleEnum.MASTER) {
                return;
            }
            try {
                replicationMsgEventQueue.put(replicationMsgEvent);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}

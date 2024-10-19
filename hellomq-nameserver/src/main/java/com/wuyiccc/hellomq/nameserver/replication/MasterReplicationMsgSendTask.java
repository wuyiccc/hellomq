package com.wuyiccc.hellomq.nameserver.replication;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.ReplicationMsgEvent;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author wuyiccc
 * @date 2024/10/16 23:04
 */
public class MasterReplicationMsgSendTask extends ReplicationTask {

    public MasterReplicationMsgSendTask(String taskName) {
        super(taskName);
    }

    @Override
    void startTask() {

        while (true) {

            try {

                ReplicationMsgEvent replicationMsgEvent = CommonCache.getReplicationMsgQueueManager().getReplicationMsgEventQueue().take();

                byte[] body = JsonUtils.toJsonBytes(replicationMsgEvent);
                Map<String, ChannelHandlerContext> channelHandlerContextMap = CommonCache.getReplicationChannelManager().getChannelHandlerContextMap();

                //
                for (String reqId : channelHandlerContextMap.keySet()) {
                    channelHandlerContextMap.get(reqId).writeAndFlush(new TcpMsg(NameServerEventCodeEnum.MASTER_REPLICATION_MSG.getCode(), body));
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package com.wuyiccc.hellomq.nameserver.replication;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.dto.SlaveAckDTO;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
import com.wuyiccc.hellomq.common.utils.IdUtils;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.config.MasterSlavingReplicationProperties;
import com.wuyiccc.hellomq.nameserver.enums.MasterSlaveReplicationTypeEnum;
import com.wuyiccc.hellomq.nameserver.event.model.ReplicationMsgEvent;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

            MasterSlavingReplicationProperties masterSlavingReplicationProperties = CommonCache.getNameServerProperties().getMasterSlavingReplicationProperties();
            String type = masterSlavingReplicationProperties.getType();
            MasterSlaveReplicationTypeEnum replicationTypeEnum = MasterSlaveReplicationTypeEnum.of(type);

            try {

                ReplicationMsgEvent replicationMsgEvent = CommonCache.getReplicationMsgQueueManager().getReplicationMsgEventQueue().take();
                replicationMsgEvent.setMsgId(IdUtils.generateUUId());
                Channel brokerChannel = replicationMsgEvent.getChannelHandlerContext().channel();

                Map<String, ChannelHandlerContext> validSlaveChannelMap = CommonCache.getReplicationChannelManager().getValidSlaveChannelMap();
                int needSlaveAckCount = validSlaveChannelMap.keySet().size();
                // 判断当前采用的同步模式是哪种
                if (replicationTypeEnum == MasterSlaveReplicationTypeEnum.ASYNC) {
                    this.sendMsgToSlave(replicationMsgEvent);
                    brokerChannel.writeAndFlush(new TcpMsg(NameServerResponseCodeEnum.REGISTRY_SUCCESS.getCode()
                            , NameServerResponseCodeEnum.REGISTRY_SUCCESS.getDesc().getBytes(StandardCharsets.UTF_8)));
                } else if (replicationTypeEnum == MasterSlaveReplicationTypeEnum.SYNC) {
                    this.sendMsgToSlave(replicationMsgEvent);
                    // 需要接受到多少个ack的次数
                    inputMsgToAckMap(replicationMsgEvent, needSlaveAckCount);
                } else if (replicationTypeEnum == MasterSlaveReplicationTypeEnum.HALF_SYNC) {
                    this.sendMsgToSlave(replicationMsgEvent);
                    // 需要接受到多少个ack的次数
                    inputMsgToAckMap(replicationMsgEvent, needSlaveAckCount / 2);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 发送数据给从节点
     */
    private void sendMsgToSlave(ReplicationMsgEvent replicationMsgEvent) {


        Map<String, ChannelHandlerContext> validSlaveChannelMap = CommonCache.getReplicationChannelManager().getValidSlaveChannelMap();

        for (String reqId : validSlaveChannelMap.keySet()) {

            replicationMsgEvent.setChannelHandlerContext(null);
            byte[] body = JsonUtils.toJsonBytes(replicationMsgEvent);

            // 异步复制直接发送给从节点, 然后告知broker注册成功
            validSlaveChannelMap.get(reqId).writeAndFlush(new TcpMsg(NameServerEventCodeEnum.MASTER_REPLICATION_MSG.getCode(), body));
        }
    }

    /**
     * 将主节点发送出去的数据注入到一个map中
     */
    private void inputMsgToAckMap(ReplicationMsgEvent replicationMsgEvent, int needAckCount) {

        CommonCache.getAckMap().put(replicationMsgEvent.getMsgId(), new SlaveAckDTO(new AtomicInteger(needAckCount / 2)
                , replicationMsgEvent.getChannelHandlerContext()));
    }
}

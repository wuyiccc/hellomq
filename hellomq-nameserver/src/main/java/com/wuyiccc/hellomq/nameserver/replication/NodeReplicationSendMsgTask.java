package com.wuyiccc.hellomq.nameserver.replication;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.dto.NodeAckDTO;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.utils.IdUtils;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.NodeReplicationMsgEvent;
import com.wuyiccc.hellomq.nameserver.event.model.ReplicationMsgEvent;
import io.netty.channel.Channel;

/**
 * @author wuyiccc
 * @date 2024/10/20 18:13
 * <p>
 * 链式复制中非尾部节点发送数据给下一个节点的任务
 */
public class NodeReplicationSendMsgTask extends ReplicationTask {


    public NodeReplicationSendMsgTask(String taskName) {
        super(taskName);
    }

    @Override
    void startTask() {


        while (true) {

            try {
                ReplicationMsgEvent replicationMsgEvent = CommonCache.getReplicationMsgQueueManager().getReplicationMsgEventQueue().take();
                replicationMsgEvent.setMsgId(IdUtils.generateUUId());

                // 转为trace复制的对象
                NodeReplicationMsgEvent nodeReplicationMsgEvent = new NodeReplicationMsgEvent();
                nodeReplicationMsgEvent.setMsgId(replicationMsgEvent.getMsgId());
                nodeReplicationMsgEvent.setServiceInstance(replicationMsgEvent.getServiceInstance());
                // 发送给下一个节点
                Channel nextNodeChannel = CommonCache.getConnectNodeChannel();
                if (nextNodeChannel.isActive()) {
                    TcpMsg nextNodeMsg = new TcpMsg(NameServerEventCodeEnum.NODE_REPLICATION_MSG.getCode()
                            , JsonUtils.toJsonBytes(nodeReplicationMsgEvent));
                    nextNodeChannel.writeAndFlush(nextNodeMsg);
                }

                NodeAckDTO nodeAckDTO = new NodeAckDTO();
                nodeAckDTO.setChannelHandlerContext(replicationMsgEvent.getChannelHandlerContext());

                CommonCache.getNodeAckMap().put(replicationMsgEvent.getMsgId(), nodeAckDTO);

            } catch (InterruptedException e) {

                throw new RuntimeException(e);
            }
        }
    }
}

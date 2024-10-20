package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.config.TraceReplicationProperties;
import com.wuyiccc.hellomq.nameserver.event.model.NodeReplicationAckMsgEvent;
import com.wuyiccc.hellomq.nameserver.event.model.NodeReplicationMsgEvent;
import com.wuyiccc.hellomq.nameserver.event.model.ReplicationMsgEvent;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;

/**
 * @author wuyiccc
 * @date 2024/10/20 22:18
 */
public class NodeReplicationMsgListener implements Listener<NodeReplicationMsgEvent> {

    private static final Logger log = LoggerFactory.getLogger(NodeReplicationMsgListener.class);


    @Override
    public void onReceive(NodeReplicationMsgEvent event) throws Exception {


        log.info("接收到前一个节点的复制数据, NodeReplicationMsgEvent: {}", event);

        ServiceInstance serviceInstance = event.getServiceInstance();
        // 接收到上一个节点同步过来的数据, 然后存入本地内存
        CommonCache.getServiceInstanceManager().put(serviceInstance);
        // 将数据发送给下一个节点
        ReplicationMsgEvent replicationMsgEvent = new ReplicationMsgEvent();
        replicationMsgEvent.setServiceInstance(serviceInstance);
        CommonCache.getReplicationMsgQueueManager().put(replicationMsgEvent);

        TraceReplicationProperties traceReplicationProperties = CommonCache.getNameServerProperties().getTraceReplicationProperties();

        if (StringUtils.isBlank(traceReplicationProperties.getNextNode())) {

            // 如果是尾部节点, 那么需要发送ack信号给上一个节点
            NodeReplicationAckMsgEvent nodeReplicationAckMsgEvent = new NodeReplicationAckMsgEvent();
            nodeReplicationAckMsgEvent.setNodeIp(Inet4Address.getLocalHost().getHostAddress());
            nodeReplicationAckMsgEvent.setNodePort(traceReplicationProperties.getPort());
            nodeReplicationAckMsgEvent.setMsgId(event.getMsgId());

            CommonCache.getPreNodeChannel().writeAndFlush(new TcpMsg(NameServerEventCodeEnum.NODE_REPLICATION_ACK_MSG.getCode()
                    , JsonUtils.toJsonBytes(nodeReplicationAckMsgEvent)));
            log.info("当前节点已是尾部节点, 发送ack信息给上一个节点: {}", nodeReplicationAckMsgEvent);
        }


    }


}

package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.dto.NodeAckDTO;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.NodeReplicationAckMsgEvent;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wuyiccc
 * @date 2024/10/20 23:12
 */
public class NodeReplicationAckMsgEventListener implements Listener<NodeReplicationAckMsgEvent> {

    private static final Logger log = LoggerFactory.getLogger(NodeReplicationAckMsgEventListener.class);


    @Override
    public void onReceive(NodeReplicationAckMsgEvent event) throws Exception {


        log.info("trace节点接收到子节点ack信息, NodeReplicationAckMsgEvent: {}", event);
        log.info("当前ackMap数据为: {}", JsonUtils.toJsonStr(CommonCache.getNodeAckMap()));

        boolean isHeadNode = CommonCache.getPreNodeChannel() == null;

        if (isHeadNode) {
            // 如果是头节点, 告诉broker客户端整个链路同步复制完成
            log.info("本次需要移除的msgId: {}", event.getMsgId());
            NodeAckDTO nodeAckDTO = CommonCache.getNodeAckMap().get(event.getMsgId());
            Channel brokerChannel = nodeAckDTO.getChannelHandlerContext().channel();
            if (!brokerChannel.isActive()) {
                throw new RuntimeException("broker connection is broken!");
            }
            CommonCache.getNodeAckMap().remove(event.getMsgId());
            brokerChannel.writeAndFlush(new TcpMsg(NameServerResponseCodeEnum.REGISTRY_SUCCESS.getCode()
                    , NameServerResponseCodeEnum.REGISTRY_SUCCESS.getDesc().getBytes(StandardCharsets.UTF_8)));
            log.info("当前节点已是头节点, 向broker发送确认注册成功消息");

        } else {
            // 当前节点不是中间节点, 还需要告知上一个节点同步完成
            CommonCache.getPreNodeChannel().writeAndFlush(new TcpMsg(NameServerEventCodeEnum.NODE_REPLICATION_ACK_MSG.getCode()
            , JsonUtils.toJsonBytes(event)));
        }

    }
}

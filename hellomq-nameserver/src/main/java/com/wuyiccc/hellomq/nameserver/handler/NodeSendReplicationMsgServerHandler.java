package com.wuyiccc.hellomq.nameserver.handler;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.event.EventBus;
import com.wuyiccc.hellomq.nameserver.event.model.Event;
import com.wuyiccc.hellomq.nameserver.event.model.NodeReplicationAckMsgEvent;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author wuyiccc
 * @date 2024/10/20 16:23
 *
 * 下一个节点数据返回的内容接收器
 */
@ChannelHandler.Sharable
public class NodeSendReplicationMsgServerHandler extends SimpleChannelInboundHandler {

    private EventBus eventBus;

    public NodeSendReplicationMsgServerHandler(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.init();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        TcpMsg tcpMsg = (TcpMsg) msg;
        int code = tcpMsg.getCode();
        byte[] body = tcpMsg.getBody();
        Event event = null;

        if (NameServerEventCodeEnum.NODE_REPLICATION_ACK_MSG.getCode() == code) {
            event = JsonUtils.toBean(body, NodeReplicationAckMsgEvent.class);
        }
        event.setChannelHandlerContext(ctx);
        eventBus.publish(event);

    }
}

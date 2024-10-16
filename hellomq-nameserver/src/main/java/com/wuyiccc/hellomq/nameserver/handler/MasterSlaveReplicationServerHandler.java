package com.wuyiccc.hellomq.nameserver.handler;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.nameserver.event.EventBus;
import com.wuyiccc.hellomq.nameserver.event.model.Event;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuyiccc
 * @date 2024/10/5 15:29
 */
@ChannelHandler.Sharable
public class MasterSlaveReplicationServerHandler extends SimpleChannelInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(MasterSlaveReplicationServerHandler.class);

    private EventBus eventBus;

    public MasterSlaveReplicationServerHandler(EventBus eventBus) {
        this.eventBus = eventBus;
        this.eventBus.init();
    }

    // 网络请求的接收(netty)
    // 事件发布器的实现
    // 事件处理器的实现
    // 数据存储
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        TcpMsg tcpMsg = (TcpMsg) msg;
        int code = tcpMsg.getCode();

        byte[] body = tcpMsg.getBody();

        // 从节点发起链接, 在master端通过密码进行验证, 建立链接
        Event event = null;

        event.setChannelHandlerContext(ctx);
        eventBus.publish(event);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

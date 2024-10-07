package com.wuyiccc.hellomq.nameserver.handler;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.event.EventBus;
import com.wuyiccc.hellomq.nameserver.event.model.Event;
import com.wuyiccc.hellomq.nameserver.event.model.HeartBeatEvent;
import com.wuyiccc.hellomq.nameserver.event.model.RegistryEvent;
import com.wuyiccc.hellomq.nameserver.event.model.UnRegistryEvent;
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
public class TcpNettyServerHandler extends SimpleChannelInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(TcpNettyServerHandler.class);

    private EventBus eventBus;

    public TcpNettyServerHandler(EventBus eventBus) {
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
        Event event = null;

        if (NameServerEventCodeEnum.REGISTRY.getCode() == code) {
            event = JsonUtils.jsonToPojo(new String(body), RegistryEvent.class);
        } else if (NameServerEventCodeEnum.UN_REGISTRY.getCode() == code) {
            event = JsonUtils.jsonToPojo(new String(body), UnRegistryEvent.class);
        } else if (NameServerEventCodeEnum.HEART_BEAT.getCode() == code) {
            event = new HeartBeatEvent();
        }

        event.setChannelHandlerContext(ctx);
        eventBus.publish(event);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        UnRegistryEvent unRegistryEvent = new UnRegistryEvent();
        unRegistryEvent.setChannelHandlerContext(ctx);
        eventBus.publish(unRegistryEvent);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}

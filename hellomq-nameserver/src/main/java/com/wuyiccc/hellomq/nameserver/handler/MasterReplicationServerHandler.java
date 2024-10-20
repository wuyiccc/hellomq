package com.wuyiccc.hellomq.nameserver.handler;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.event.EventBus;
import com.wuyiccc.hellomq.nameserver.event.model.Event;
import com.wuyiccc.hellomq.nameserver.event.model.SlaveHeartBeatEvent;
import com.wuyiccc.hellomq.nameserver.event.model.SlaveReplicationMsgAckEvent;
import com.wuyiccc.hellomq.nameserver.event.model.StartReplicationEvent;
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
public class MasterReplicationServerHandler extends SimpleChannelInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(MasterReplicationServerHandler.class);

    private EventBus eventBus;

    public MasterReplicationServerHandler(EventBus eventBus) {
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
        if (NameServerEventCodeEnum.START_REPLICATION.getCode() == code) {
            event = JsonUtils.toBean(new String(body), StartReplicationEvent.class);
        } else if (NameServerEventCodeEnum.SLAVE_HEART_BEAT.getCode() == code) {
            event = new SlaveHeartBeatEvent();
        } else if (NameServerEventCodeEnum.SLAVE_REPLICATION_ACK_MSG.getCode() == code) {
            event = JsonUtils.toBean(body, SlaveReplicationMsgAckEvent.class);
        } else {
            return;
        }

        event.setChannelHandlerContext(ctx);
        eventBus.publish(event);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {


        log.info("channelInactive执行 ctx: {}", ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught执行", cause);
        super.exceptionCaught(ctx, cause);
    }
}

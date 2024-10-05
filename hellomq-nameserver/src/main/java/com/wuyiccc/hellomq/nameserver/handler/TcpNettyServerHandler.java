package com.wuyiccc.hellomq.nameserver.handler;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
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

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

        TcpMsg tcpMsg = (TcpMsg) msg;

        // 解析成特定的事件, 然后发送事件消息出去
        log.info(JsonUtils.objectToJson(tcpMsg));
    }
}

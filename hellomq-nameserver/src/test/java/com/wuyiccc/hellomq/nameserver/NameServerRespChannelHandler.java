package com.wuyiccc.hellomq.nameserver;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ChannelHandler.Sharable
public class NameServerRespChannelHandler extends SimpleChannelInboundHandler {

    private static final Logger log = LoggerFactory.getLogger(NameServerRespChannelHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        TcpMsg tcpMsg = (TcpMsg) msg;

        log.info("resp:" + JsonUtils.toJsonStr(tcpMsg));
    }
}

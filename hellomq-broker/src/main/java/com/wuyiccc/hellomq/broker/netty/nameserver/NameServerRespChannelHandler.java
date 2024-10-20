package com.wuyiccc.hellomq.broker.netty.nameserver;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
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
        if (NameServerResponseCodeEnum.REGISTRY_SUCCESS.getCode() == tcpMsg.getCode()) {
            // 注册成功, 开启心跳定时任务上报给nameserver
            log.info("注册成功, 开启心跳任务");
            CommonCache.getHeartBeatTaskManager().startTask();
        } else if (NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getCode() == tcpMsg.getCode()) {
            // 验证失败, 抛出异常
            throw new RuntimeException("error nameserver user or password");
        }

    }
}

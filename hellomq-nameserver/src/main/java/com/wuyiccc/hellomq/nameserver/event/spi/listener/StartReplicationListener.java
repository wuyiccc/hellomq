package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.constants.BaseConstants;
import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import com.wuyiccc.hellomq.common.constants.StrConstants;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.Event;
import com.wuyiccc.hellomq.nameserver.event.model.StartReplicationEvent;
import com.wuyiccc.hellomq.nameserver.utils.NameServerUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author wuyiccc
 * @date 2024/10/16 21:40
 */
public class StartReplicationListener implements Listener<StartReplicationEvent> {

    @Override
    public void onReceive(StartReplicationEvent event) throws Exception {

        boolean isVerify = NameServerUtils.isVerify(event.getUser(), event.getPassword());
        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();

        if (!isVerify) {

            TcpMsg tcpMsg = new TcpMsg(NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getCode()
                    , NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getDesc().getBytes(StandardCharsets.UTF_8)
            );
            channelHandlerContext.writeAndFlush(tcpMsg);
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected!");
        }

        InetSocketAddress inetSocketAddress = (InetSocketAddress) channelHandlerContext.channel().remoteAddress();
        event.setSlaveIp(inetSocketAddress.getHostString());
        event.setSlavePort(String.valueOf(inetSocketAddress.getPort()));

        String reqId = event.getSlaveIp() + StrConstants.COLON + event.getSlavePort();
        channelHandlerContext.attr(AttributeKey.valueOf(BaseConstants.REQ_ID)).set(reqId);
        CommonCache.getReplicationChannelManager().put(reqId, channelHandlerContext);

        TcpMsg tcpMsg = new TcpMsg(NameServerEventCodeEnum.MASTER_START_REPLICATION_ACK.getCode(), new byte[0]);
        channelHandlerContext.writeAndFlush(tcpMsg);
    }
}

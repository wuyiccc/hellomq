package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.constants.BaseConstants;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.core.PropertiesLoader;
import com.wuyiccc.hellomq.nameserver.event.model.Event;
import com.wuyiccc.hellomq.nameserver.event.model.UnRegistryEvent;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author wuyiccc
 * @date 2024/10/6 09:09
 */
public class UnRegistryListener implements Listener<UnRegistryEvent> {
    @Override
    public void onReceive(UnRegistryEvent event) throws IllegalAccessException {

        String ip = event.getBrokerIp();
        Integer port = event.getBrokerPort();

        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();
        if (channelHandlerContext.attr(AttributeKey.valueOf(BaseConstants.REQ_ID)).get() == null) {

            TcpMsg tcpMsg = new TcpMsg(NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getCode()
                    , NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getDesc().getBytes(StandardCharsets.UTF_8));
            channelHandlerContext.writeAndFlush(tcpMsg);
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected!");
        }

        if (!StringUtils.isEmpty(ip) && port != null) {
            boolean removeStatus = CommonCache.getServiceInstanceManager().remove(ip, port) != null;
        }

    }
}

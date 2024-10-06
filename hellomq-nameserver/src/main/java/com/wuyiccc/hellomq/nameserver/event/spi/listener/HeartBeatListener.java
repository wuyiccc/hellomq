package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.constants.BaseConstants;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.HeartBeatEvent;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.nio.charset.StandardCharsets;

/**
 * @author wuyiccc
 * @date 2024/10/6 09:08
 */
public class HeartBeatListener implements Listener<HeartBeatEvent> {
    @Override
    public void onReceive(HeartBeatEvent event) throws IllegalAccessException {

        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();

        if (channelHandlerContext.attr(AttributeKey.valueOf(BaseConstants.REQ_ID)).get() == null) {

            TcpMsg tcpMsg = new TcpMsg(NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getCode()
                    , NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getDesc().getBytes(StandardCharsets.UTF_8));
            channelHandlerContext.writeAndFlush(tcpMsg);
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected!");

        }
        // 之前认证过
        long currentTimestamp = System.currentTimeMillis();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setBrokerIp(event.getBrokerIp());
        serviceInstance.setBrokerPort(event.getBrokerPort());
        serviceInstance.setLastHeartBeatTime(currentTimestamp);
        CommonCache.getServiceInstanceManager().putIfExist(serviceInstance);

    }
}

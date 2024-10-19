package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.constants.BaseConstants;
import com.wuyiccc.hellomq.common.constants.StrConstants;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.RegistryEvent;
import com.wuyiccc.hellomq.nameserver.event.model.ReplicationMsgEvent;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstance;
import com.wuyiccc.hellomq.nameserver.utils.NameServerUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.netty.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author wuyiccc
 * @date 2024/10/6 09:09
 */
public class RegistryListener implements Listener<RegistryEvent> {

    private static final Logger log = LoggerFactory.getLogger(RegistryListener.class);


    @Override
    public void onReceive(RegistryEvent event) throws IllegalAccessException {

        // 安全认证
        boolean isVerify = NameServerUtils.isVerify(event.getUser(), event.getPassword());
        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();

        if (!isVerify) {

            channelHandlerContext.writeAndFlush(new TcpMsg(NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getCode()
                    , NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getDesc().getBytes(StandardCharsets.UTF_8)));
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected");
        }
        log.info("注册事件接收: {}", event);
        channelHandlerContext.attr(AttributeKey.valueOf(BaseConstants.REQ_ID)).set(event.getBrokerIp() + StrConstants.COLON + event.getBrokerPort());

        long currentTimestamp = System.currentTimeMillis();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setBrokerIp(event.getBrokerIp());
        serviceInstance.setBrokerPort(event.getBrokerPort());
        serviceInstance.setFirstRegistryTime(currentTimestamp);
        CommonCache.getServiceInstanceManager().put(serviceInstance);

        // 如果当前是主从复制模式, 而且当前角色是主节点, 那么就往复制队列里面放元素
        ReplicationMsgEvent replicationMsgEvent = new ReplicationMsgEvent();
        replicationMsgEvent.setServiceInstance(serviceInstance);
        CommonCache.getReplicationMsgQueueManager().put(replicationMsgEvent);

        TcpMsg registrySuccessResponseMsg = new TcpMsg(NameServerResponseCodeEnum.REGISTRY_SUCCESS.getCode()
                , NameServerResponseCodeEnum.REGISTRY_SUCCESS.getDesc().getBytes(StandardCharsets.UTF_8));
        channelHandlerContext.writeAndFlush(registrySuccessResponseMsg);

    }
}

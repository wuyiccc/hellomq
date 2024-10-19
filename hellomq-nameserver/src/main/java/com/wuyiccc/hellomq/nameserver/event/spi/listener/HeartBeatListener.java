package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.constants.BaseConstants;
import com.wuyiccc.hellomq.common.constants.StrConstants;
import com.wuyiccc.hellomq.common.enums.NameServerResponseCodeEnum;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.HeartBeatEvent;
import com.wuyiccc.hellomq.nameserver.event.model.ReplicationMsgEvent;
import com.wuyiccc.hellomq.nameserver.handler.TcpNettyServerHandler;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstance;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author wuyiccc
 * @date 2024/10/6 09:08
 */
public class HeartBeatListener implements Listener<HeartBeatEvent> {

    private static final Logger log = LoggerFactory.getLogger(HeartBeatListener.class);


    @Override
    public void onReceive(HeartBeatEvent event) throws IllegalAccessException {

        ChannelHandlerContext channelHandlerContext = event.getChannelHandlerContext();

        Object reqId = channelHandlerContext.attr(AttributeKey.valueOf(BaseConstants.REQ_ID)).get();
        if (reqId == null) {

            TcpMsg tcpMsg = new TcpMsg(NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getCode()
                    , NameServerResponseCodeEnum.ERROR_USER_OR_PASSWORD.getDesc().getBytes(StandardCharsets.UTF_8));
            channelHandlerContext.writeAndFlush(tcpMsg);
            channelHandlerContext.close();
            throw new IllegalAccessException("error account to connected!");
        }

        log.info("接收到心跳数据: {}", event);

        String brokerIdentifyStr = (String) reqId;
        String[] brokerInfoArr = brokerIdentifyStr.split(StrConstants.COLON);
        // 之前认证过
        long currentTimestamp = System.currentTimeMillis();
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setBrokerIp(brokerInfoArr[0]);
        serviceInstance.setBrokerPort(Integer.valueOf(brokerInfoArr[1]));
        serviceInstance.setLastHeartBeatTime(currentTimestamp);

        log.info("心跳更新数据为: {}", serviceInstance);

        CommonCache.getServiceInstanceManager().putIfExist(serviceInstance);

        ReplicationMsgEvent replicationMsgEvent = new ReplicationMsgEvent();
        replicationMsgEvent.setServiceInstance(serviceInstance);
        CommonCache.getReplicationMsgQueueManager().put(replicationMsgEvent);

    }
}

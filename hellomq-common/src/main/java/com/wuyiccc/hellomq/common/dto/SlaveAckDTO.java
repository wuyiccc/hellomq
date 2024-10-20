package com.wuyiccc.hellomq.common.dto;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuyiccc
 * @date 2024/10/20 10:18
 */
public class SlaveAckDTO {

    // 信息需要从节点ack的节点数量
    private AtomicInteger needAckTime;


    // 信息发送方对应的broker的channel
    private ChannelHandlerContext brokerChannel;

    public SlaveAckDTO(AtomicInteger needAckTime, ChannelHandlerContext brokerChannel) {
        this.needAckTime = needAckTime;
        this.brokerChannel = brokerChannel;
    }

    public AtomicInteger getNeedAckTime() {
        return needAckTime;
    }

    public void setNeedAckTime(AtomicInteger needAckTime) {
        this.needAckTime = needAckTime;
    }

    public ChannelHandlerContext getBrokerChannel() {
        return brokerChannel;
    }

    public void setBrokerChannel(ChannelHandlerContext brokerChannel) {
        this.brokerChannel = brokerChannel;
    }
}

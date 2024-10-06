package com.wuyiccc.hellomq.nameserver.event.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author wuyiccc
 * @date 2024/10/5 22:40
 */
public abstract class Event {

    private long timestamp;


    private ChannelHandlerContext channelHandlerContext;

    private String brokerIp;

    private Integer brokerPort;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }


    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(Integer brokerPort) {
        this.brokerPort = brokerPort;
    }
}

package com.wuyiccc.hellomq.nameserver.event.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author wuyiccc
 * @date 2024/10/5 22:40
 */
public abstract class Event {

    private long timestamp;


    private ChannelHandlerContext channelHandlerContext;
}

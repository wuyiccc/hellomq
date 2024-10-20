package com.wuyiccc.hellomq.common.dto;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author wuyiccc
 * @date 2024/10/20 23:23
 *
 * 链式复制中的ack对象
 */
public class NodeAckDTO {

    private ChannelHandlerContext channelHandlerContext;

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }
}

package com.wuyiccc.hellomq.nameserver.event.model;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author wuyiccc
 * @date 2024/10/5 22:40
 */
public abstract class Event {


    private String msgId;

    private ChannelHandlerContext channelHandlerContext;




    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        this.channelHandlerContext = channelHandlerContext;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    @Override
    public String toString() {
        return "Event{" +
                "msgId='" + msgId + '\'' +
                ", channelHandlerContext=" + channelHandlerContext +
                '}';
    }
}

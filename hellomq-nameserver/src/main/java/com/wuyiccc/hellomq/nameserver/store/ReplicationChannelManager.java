package com.wuyiccc.hellomq.nameserver.store;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wuyiccc
 * @date 2024/10/16 21:23
 */
public class ReplicationChannelManager {

    private static Map<String, ChannelHandlerContext> channelHandlerContextMap = new ConcurrentHashMap<>();

    public static Map<String, ChannelHandlerContext> getChannelHandlerContextMap() {

        return channelHandlerContextMap;
    }

    public void put(String reqId, ChannelHandlerContext channelHandlerContext) {

        channelHandlerContextMap.put(reqId, channelHandlerContext);
    }

    public ChannelHandlerContext get(String reqId) {

        return channelHandlerContextMap.get(reqId);
    }
}

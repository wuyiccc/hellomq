package com.wuyiccc.hellomq.nameserver.store;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;
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


    public Map<String, ChannelHandlerContext> getValidSlaveChannelMap() {

        List<String> inValidChannelReqIdList = new ArrayList<>();


        for (String reqId : channelHandlerContextMap.keySet()) {

            Channel slaveChannel = channelHandlerContextMap.get(reqId).channel();
            if (!slaveChannel.isActive()) {
                inValidChannelReqIdList.add(reqId);
            }
        }

        if (!inValidChannelReqIdList.isEmpty()) {
            for (String reqId : inValidChannelReqIdList) {

                // 移除不可用的channel
                channelHandlerContextMap.remove(reqId);
            }
        }
        return channelHandlerContextMap;
    }

}

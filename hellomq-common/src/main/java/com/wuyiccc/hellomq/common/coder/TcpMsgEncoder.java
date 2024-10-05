package com.wuyiccc.hellomq.common.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author wuyiccc
 * @date 2024/10/5 14:44
 */
public class TcpMsgEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        TcpMsg tcpMsg = (TcpMsg) msg;
        out.writeShort(tcpMsg.getMagic());
        out.writeInt(tcpMsg.getCode());
        out.writeInt(tcpMsg.getLen());
        out.writeBytes(tcpMsg.getBody());
    }
}

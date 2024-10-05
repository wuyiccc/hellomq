package com.wuyiccc.hellomq.common.coder;

import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import jdk.nashorn.internal.ir.ReturnNode;

import java.util.List;

/**
 * @author wuyiccc
 * @date 2024/10/5 14:44
 */
public class TcpMsgDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {

        if (byteBuf.readableBytes() > 2 + 4 + 4) {
            if (byteBuf.readShort() != BrokerConstants.DEFAULT_MAGIC_NUM) {
                ctx.close();
                return;
            }

            int code = byteBuf.readInt();
            int len = byteBuf.readInt();
            if (byteBuf.readableBytes() < len) {
                // 消息长度不够
                ctx.close();
                return;
            }

            byte[] body = new byte[len];
            byteBuf.readBytes(body);
            TcpMsg tcpMsg = new TcpMsg();
            tcpMsg.setMagic(BrokerConstants.DEFAULT_MAGIC_NUM);
            tcpMsg.setCode(code);
            tcpMsg.setLen(len);
            tcpMsg.setBody(body);
            out.add(tcpMsg);
        }


    }
}

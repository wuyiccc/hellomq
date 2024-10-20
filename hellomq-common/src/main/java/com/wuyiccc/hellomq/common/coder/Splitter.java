package com.wuyiccc.hellomq.common.coder;

import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author wuyiccc
 * @date 2024/7/4 20:53
 * 该handler不能共享，因为需要维护每个channel的数据分片内容
 */
public class Splitter extends LengthFieldBasedFrameDecoder {

    private static final int LENGTH_FIELD_OFFSET = 2 + 4;

    private static final int LENGTH_FIELD_LENGTH = 4;

    public Splitter() {

        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 屏蔽非本协议的客户端
        if (in.getShort(in.readerIndex()) != BrokerConstants.DEFAULT_MAGIC_NUM) {
            System.out.println("检测到异常连接，关闭异常客户端");
            ctx.channel().close();
            return null;
        }

        return super.decode(ctx, in);
    }
}

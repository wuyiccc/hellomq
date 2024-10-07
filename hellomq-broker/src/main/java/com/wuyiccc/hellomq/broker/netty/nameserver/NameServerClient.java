package com.wuyiccc.hellomq.broker.netty.nameserver;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.common.coder.TcpMsgDecoder;
import com.wuyiccc.hellomq.common.coder.TcpMsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuyiccc
 * @date 2024/10/7 16:05
 * <p>
 * 负责与nameserver服务端创建长连接, 支持链接创建，支持链接重试机制
 */
public class NameServerClient {
    private static final Logger log = LoggerFactory.getLogger(NameServerClient.class);

    private EventLoopGroup clientGroup = new NioEventLoopGroup();

    private Bootstrap bootstrap = new Bootstrap();

    private Channel channel;


    public void initConnection() {

        String ip = CommonCache.getGlobalProperties().getNameserverIp();
        Integer port = CommonCache.getGlobalProperties().getNameserverPort();
        if (StringUtils.isBlank(ip) || port == null || port < 0) {
            throw new RuntimeException("error port or ip");
        }

        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new TcpMsgDecoder());
                ch.pipeline().addLast(new TcpMsgEncoder());
                ch.pipeline().addLast(new NameServerRespChannelHandler());
            }
        });

        ChannelFuture channelFuture;
        try {
            channelFuture = bootstrap.connect(ip, port).sync();
            channel = channelFuture.channel();
            log.info("success connected to nameserver");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Channel getChannel() {

        if (channel == null) {
            throw new RuntimeException("channel has not been connected!");
        }
        return channel;
    }


}

package com.wuyiccc.hellomq.nameserver;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.coder.TcpMsgDecoder;
import com.wuyiccc.hellomq.common.coder.TcpMsgEncoder;
import com.wuyiccc.hellomq.common.constants.NameServerConstants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @Author idea
 * @Date: Created in 13:25 2024/5/4
 * @Description
 */
public class TestNameServerSuite {

    private static final Logger log = LoggerFactory.getLogger(NameServerRespChannelHandler.class);

    private EventLoopGroup clientGroup = new NioEventLoopGroup();
    private Bootstrap bootstrap = new Bootstrap();
    private Channel channel;
    private String DEFAULT_NAMESERVER_IP = "127.0.0.1";

    @Before
    public void setUp() {
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
        ChannelFuture channelFuture = null;
        try {
            channelFuture = bootstrap.connect(DEFAULT_NAMESERVER_IP, NameServerConstants.DEFAULT_NAMESERVER_PORT).sync();
            channel = channelFuture.channel();
            log.info("success connected to nameserver!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSendMsg() {
        for (int i = 0; i < 100; i++) {
            try {
                System.out.println("isActive:" + channel.isActive());
                TimeUnit.SECONDS.sleep(1);
                String msgBody = "this is client test string";
                TcpMsg tcpMsg = new TcpMsg(1, msgBody.getBytes());
                channel.writeAndFlush(tcpMsg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

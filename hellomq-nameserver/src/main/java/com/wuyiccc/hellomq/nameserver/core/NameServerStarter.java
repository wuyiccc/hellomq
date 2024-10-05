package com.wuyiccc.hellomq.nameserver.core;

import com.wuyiccc.hellomq.common.coder.TcpMsgDecoder;
import com.wuyiccc.hellomq.common.coder.TcpMsgEncoder;
import com.wuyiccc.hellomq.nameserver.handler.TcpNettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuyiccc
 * @date 2024/10/5 14:28
 */
public class NameServerStarter {

    private static final Logger log = LoggerFactory.getLogger(NameServerStarter.class);


    private int port;


    public NameServerStarter(int port) {
        this.port = port;
    }

    public void startServer() throws InterruptedException {
        // 构建netty服务
        // 注入编解码器
        // 注入特定的handler
        // 启动netty服务

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 处理网络io中的read&write事件
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new TcpMsgDecoder());
                ch.pipeline().addLast(new TcpMsgEncoder());
                ch.pipeline().addLast(new TcpNettyServerHandler());
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            log.info("nameserver is closed");
        }));
        ChannelFuture cf = bootstrap.bind(port).sync();
        log.info("start nameserver application on port: " + port);
        cf.channel().closeFuture().sync();
    }
}

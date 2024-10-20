package com.wuyiccc.hellomq.broker.netty.nameserver;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.broker.config.GlobalProperties;
import com.wuyiccc.hellomq.common.coder.Splitter;
import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.coder.TcpMsgDecoder;
import com.wuyiccc.hellomq.common.coder.TcpMsgEncoder;
import com.wuyiccc.hellomq.common.dto.RegistryDTO;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

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

        NameServerRespChannelHandler nameServerRespChannelHandler = new NameServerRespChannelHandler();

        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new Splitter());
                ch.pipeline().addLast(new TcpMsgDecoder());
                ch.pipeline().addLast(new TcpMsgEncoder());
                ch.pipeline().addLast(nameServerRespChannelHandler);
            }
        });

        // jvm停止的时候自动关闭clientGroup
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            clientGroup.shutdownGracefully();
            log.info("nameserver client is closed");
        }));

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

    public void sendRegistryMsg() {

        RegistryDTO registryDTO = new RegistryDTO();
        try {
            registryDTO.setBrokerIp(Inet4Address.getLocalHost().getHostAddress());
            GlobalProperties globalProperties = CommonCache.getGlobalProperties();
            registryDTO.setBrokerPort(globalProperties.getBrokerPort());
            registryDTO.setUser(globalProperties.getNameserverUser());
            registryDTO.setPassword(globalProperties.getNameserverPassword());

            byte[] body = JsonUtils.toJsonStr(registryDTO).getBytes(StandardCharsets.UTF_8);

            TcpMsg tcpMsg = new TcpMsg(NameServerEventCodeEnum.REGISTRY.getCode(), body);
            channel.writeAndFlush(tcpMsg);
            log.info("发送注册事件");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }


}

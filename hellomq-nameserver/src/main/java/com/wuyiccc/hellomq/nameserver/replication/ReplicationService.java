package com.wuyiccc.hellomq.nameserver.replication;

import com.wuyiccc.hellomq.common.coder.Splitter;
import com.wuyiccc.hellomq.common.coder.TcpMsgDecoder;
import com.wuyiccc.hellomq.common.coder.TcpMsgEncoder;
import com.wuyiccc.hellomq.common.constants.StrConstants;
import com.wuyiccc.hellomq.common.utils.AssertUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.config.MasterSlavingReplicationProperties;
import com.wuyiccc.hellomq.nameserver.config.NameServerProperties;
import com.wuyiccc.hellomq.nameserver.config.TraceReplicationProperties;
import com.wuyiccc.hellomq.nameserver.enums.ReplicationModeEnum;
import com.wuyiccc.hellomq.nameserver.enums.ReplicationRoleEnum;
import com.wuyiccc.hellomq.nameserver.event.EventBus;
import com.wuyiccc.hellomq.nameserver.handler.MasterReplicationServerHandler;
import com.wuyiccc.hellomq.nameserver.handler.NodeSendReplicationMsgServerHandler;
import com.wuyiccc.hellomq.nameserver.handler.NodeWriteMsgReplicationServerHandler;
import com.wuyiccc.hellomq.nameserver.handler.SlaveReplicationServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuyiccc
 * @date 2024/10/15 23:15
 */
public class ReplicationService {


    private static final Logger log = LoggerFactory.getLogger(ReplicationService.class);


    public ReplicationModeEnum checkProperties() {

        NameServerProperties nameServerProperties = CommonCache.getNameServerProperties();
        String mode = nameServerProperties.getReplicationMode();
        if (StringUtils.isBlank(mode)) {
            log.info("执行单机模式");
            return null;
        }

        ReplicationModeEnum replicationModeEnum = ReplicationModeEnum.of(mode);
        AssertUtils.isNotNull(replicationModeEnum, "复制模式参数异常");

        if (replicationModeEnum == ReplicationModeEnum.TRACE) {
            // 链路复制
            log.info("链路复制");
            TraceReplicationProperties traceReplicationProperties = nameServerProperties.getTraceReplicationProperties();
            if (traceReplicationProperties.getNextNode() != null) {
                // 中间节点/头节点
                AssertUtils.isNotNull(traceReplicationProperties.getPort(), "nameserver.replication.trace.port 配置不能为空");
            }
        } else {
            //主从复制
            MasterSlavingReplicationProperties masterSlaveReplicationProperties = nameServerProperties.getMasterSlavingReplicationProperties();
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getMaster(), "master参数不能为空");
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getRole(), "role参数不能为空");
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getType(), "type参数不能为空");
            AssertUtils.isNotNull(masterSlaveReplicationProperties.getPort(), "同步端口不能为空");
        }
        return replicationModeEnum;
    }

    public void startReplicationTask(ReplicationModeEnum replicationModeEnum) {

        //单机版本，不用做处理
        if (replicationModeEnum == null) {
            return;
        }

        int port = 0;
        NameServerProperties nameServerProperties = CommonCache.getNameServerProperties();
        ReplicationRoleEnum roleEnum = null;
        if (replicationModeEnum == ReplicationModeEnum.MASTER_SLAVE) {
            port = nameServerProperties.getMasterSlavingReplicationProperties().getPort();
            roleEnum = ReplicationRoleEnum.of(nameServerProperties.getMasterSlavingReplicationProperties().getRole());
        } else {
            TraceReplicationProperties traceReplicationProperties = nameServerProperties.getTraceReplicationProperties();
            String nextNode = traceReplicationProperties.getNextNode();
            if (nextNode == null) {
                roleEnum = ReplicationRoleEnum.TAIL_NODE;
            } else {
                roleEnum = ReplicationRoleEnum.NODE;
            }
            port = traceReplicationProperties.getPort();
        }



        if (roleEnum == ReplicationRoleEnum.MASTER) {
            startNettyServerAsync(new MasterReplicationServerHandler(new EventBus("master-replication-task")), port);
        } else if (roleEnum == ReplicationRoleEnum.SLAVE) {
            String masterAddress = nameServerProperties.getMasterSlavingReplicationProperties().getMaster();
            startNettyConnAsync(new SlaveReplicationServerHandler(new EventBus("slave-replication-task")), masterAddress);
        } else if (roleEnum == ReplicationRoleEnum.NODE) {
            String nextNodeAddress = nameServerProperties.getTraceReplicationProperties().getNextNode();
            startNettyServerAsync(new NodeWriteMsgReplicationServerHandler(new EventBus("node-write-msg-replication-task")), port);
            startNettyConnAsync(new NodeSendReplicationMsgServerHandler(new EventBus("node-send-replication-msg-task")), nextNodeAddress);
        } else if (roleEnum == ReplicationRoleEnum.TAIL_NODE) {
            startNettyServerAsync(new NodeWriteMsgReplicationServerHandler(new EventBus("node-write-msg-replication-task")), port);
        }
    }

    // 开启对目标进程的链接
    private void startNettyConnAsync(SimpleChannelInboundHandler simpleChannelInboundHandler, String address) {

        Thread thread = new Thread(() -> {
            EventLoopGroup clientGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            Channel channel;


            bootstrap.group(clientGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new Splitter());
                    ch.pipeline().addLast(new TcpMsgDecoder());
                    ch.pipeline().addLast(new TcpMsgEncoder());
                    ch.pipeline().addLast(simpleChannelInboundHandler);
                }
            });
            ChannelFuture channelFuture;
            try {

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    clientGroup.shutdownGracefully();
                    log.info("nameserver's replication connect application is closed");
                }));

                String[] addr = address.split(StrConstants.COLON);
                channelFuture = bootstrap.connect(addr[0], Integer.parseInt(addr[1])).sync();
                // 连接了master节点的channel对象, 建议保存
                channel = channelFuture.channel();
                log.info("success connected to nameserver replication");
                CommonCache.setConnectNodeChannel(channel);
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        });
        thread.start();
    }

    // 开启一个netty进程
    private void startNettyServerAsync(SimpleChannelInboundHandler simpleChannelInboundHandler, Integer port) {

        Thread thread = new Thread(() -> {
            //负责netty启动
            NioEventLoopGroup bossGroup = new NioEventLoopGroup();
            //处理网络io中的read&write事件
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new TcpMsgDecoder());
                    ch.pipeline().addLast(new TcpMsgEncoder());
                    ch.pipeline().addLast(simpleChannelInboundHandler);

                }
            });
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                log.info("nameserver's replication application is closed");
            }));
            ChannelFuture channelFuture;
            try {
                channelFuture = bootstrap.bind(port).sync();
                log.info("start nameserver's replication application on port:" + port);
                //阻塞代码
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();
    }
}

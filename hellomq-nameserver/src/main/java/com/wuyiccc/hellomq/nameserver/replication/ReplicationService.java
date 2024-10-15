package com.wuyiccc.hellomq.nameserver.replication;

import com.wuyiccc.hellomq.common.coder.TcpMsgDecoder;
import com.wuyiccc.hellomq.common.coder.TcpMsgEncoder;
import com.wuyiccc.hellomq.common.utils.AssertUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.config.MasterSlavingReplicationProperties;
import com.wuyiccc.hellomq.nameserver.config.NameServerProperties;
import com.wuyiccc.hellomq.nameserver.enums.ReplicationModeEnum;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuyiccc
 * @date 2024/10/15 23:15
 */
public class ReplicationService {


    private static final Logger log = LoggerFactory.getLogger(ReplicationService.class);


    public void checkProperties() {

        NameServerProperties nameServerProperties = CommonCache.getNameServerProperties();
        String mode = nameServerProperties.getReplicationMode();
        if (StringUtils.isBlank(mode)) {
            log.info("执行单机模式");
            return;
        }

        ReplicationModeEnum replicationModeEnum = ReplicationModeEnum.of(mode);
        AssertUtils.isNotNull(replicationModeEnum, "复制模式参数异常");

        if (replicationModeEnum == ReplicationModeEnum.TRACE) {
            // 链路复制
            log.info("链路复制");
        } else {
            //主从复制
            MasterSlavingReplicationProperties masterSlaveReplicationProperties = nameServerProperties.getMasterSlavingReplicationProperties();
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getMaster(), "master参数不能为空");
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getRole(), "role参数不能为空");
            AssertUtils.isNotBlank(masterSlaveReplicationProperties.getType(), "type参数不能为空");
            AssertUtils.isNotNull(masterSlaveReplicationProperties.getPort(), "同步端口不能为空");
        }
    }

    public void startReplicationTask(ReplicationModeEnum replicationModeEnum) {

        //单机版本，不用做处理
        if (replicationModeEnum == null) {
            return;
        }

        int port = 0;
        if (replicationModeEnum == ReplicationModeEnum.MASTER_SLAVE) {
            port = CommonCache.getNameServerProperties().getMasterSlavingReplicationProperties().getPort();
        }

        int replicationPort = port;

        Thread replicationTask = new Thread(() -> {
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
                }
            });
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                log.info("nameserver's replication application is closed");
            }));
            ChannelFuture channelFuture;
            try {
                channelFuture = bootstrap.bind(replicationPort).sync();
                log.info("start nameserver's replication application on port:" + replicationPort);
                //阻塞代码
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        replicationTask.setName("replication-task");
        replicationTask.start();
    }
}

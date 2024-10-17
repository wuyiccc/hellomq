package com.wuyiccc.hellomq.nameserver.replication;

import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import com.wuyiccc.hellomq.common.utils.JsonUtils;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.SlaveHeartBeatEvent;
import com.wuyiccc.hellomq.nameserver.event.model.StartReplicationEvent;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author wuyiccc
 * @date 2024/10/18 00:36
 * <p>
 * 从节点给主节点发送心跳数据定时任务
 */
public class SlaveReplicationHeartBeatTask extends ReplicationTask {

    private static final Logger log = LoggerFactory.getLogger(SlaveReplicationHeartBeatTask.class);

    public SlaveReplicationHeartBeatTask(String taskName) {
        super(taskName);
    }

    @Override
    public void startTask() {


        StartReplicationEvent startReplicationEvent = new StartReplicationEvent();
        startReplicationEvent.setUser(CommonCache.getNameServerProperties().getNameserverUser());
        startReplicationEvent.setPassword(CommonCache.getNameServerProperties().getNameserverPwd());
        TcpMsg startReplicationMsg = new TcpMsg(NameServerEventCodeEnum.START_REPLICATION.getCode(), JsonUtils.toJsonBytes(startReplicationEvent));
        CommonCache.getMasterConnection().writeAndFlush(startReplicationMsg);
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(3);
                //发送数据给到主节点
                Channel channel = CommonCache.getMasterConnection();
                TcpMsg tcpMsg = new TcpMsg(NameServerEventCodeEnum.SLAVE_HEART_BEAT.getCode(), JsonUtils.toJsonBytes(new SlaveHeartBeatEvent()));
                channel.writeAndFlush(tcpMsg);
                log.info("从节点发送心跳数据给master");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

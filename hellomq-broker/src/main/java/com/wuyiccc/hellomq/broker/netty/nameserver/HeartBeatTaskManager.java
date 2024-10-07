package com.wuyiccc.hellomq.broker.netty.nameserver;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.common.coder.TcpMsg;
import com.wuyiccc.hellomq.common.constants.BaseConstants;
import com.wuyiccc.hellomq.common.enums.NameServerEventCodeEnum;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wuyiccc
 * @date 2024/10/7 20:43
 */
public class HeartBeatTaskManager {

    private AtomicInteger flag = new AtomicInteger(0);

    public void startTask() {

        if (flag.getAndIncrement() > 1) {
            return;
        }

        Thread heartBeatRequestTask = new Thread(new HeartBeatRequestTask());
        heartBeatRequestTask.setName("heart-beat-request-task");
        heartBeatRequestTask.start();
    }


    private class HeartBeatRequestTask implements Runnable {

        @Override
        public void run() {

            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(BaseConstants.SECONDS_3);
                    Channel channel = CommonCache.getNameServerClient().getChannel();
                    TcpMsg tcpMsg = new TcpMsg(NameServerEventCodeEnum.HEART_BEAT.getCode(), new byte[]{});
                    channel.writeAndFlush(tcpMsg);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

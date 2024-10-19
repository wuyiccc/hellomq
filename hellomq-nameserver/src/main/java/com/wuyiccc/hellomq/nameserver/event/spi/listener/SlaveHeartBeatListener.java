package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.nameserver.event.model.SlaveHeartBeatEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuyiccc
 * @date 2024/10/19 14:43
 */
public class SlaveHeartBeatListener implements Listener<SlaveHeartBeatEvent> {

    private static final Logger log = LoggerFactory.getLogger(SlaveHeartBeatListener.class);

    @Override
    public void onReceive(SlaveHeartBeatEvent event) throws Exception {

        log.info("接收到从节点的心跳信号");
    }
}

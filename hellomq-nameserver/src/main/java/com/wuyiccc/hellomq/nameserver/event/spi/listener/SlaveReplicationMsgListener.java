package com.wuyiccc.hellomq.nameserver.event.spi.listener;

import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.event.model.ReplicationMsgEvent;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuyiccc
 * @date 2024/10/19 11:05
 *
 * 从节点专属的数据同步监听器
 */
public class SlaveReplicationMsgListener implements Listener<ReplicationMsgEvent> {

    private static final Logger log = LoggerFactory.getLogger(SlaveReplicationMsgListener.class);


    @Override
    public void onReceive(ReplicationMsgEvent event) throws Exception {


        ServiceInstance serviceInstance = event.getServiceInstance();

        // 从节点接收主节点同步数据逻辑
        CommonCache.getServiceInstanceManager().put(serviceInstance);
        log.info("从节点接收到主节点数据");
    }
}

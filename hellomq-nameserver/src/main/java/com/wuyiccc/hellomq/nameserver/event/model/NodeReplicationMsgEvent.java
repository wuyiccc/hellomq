package com.wuyiccc.hellomq.nameserver.event.model;

import com.wuyiccc.hellomq.nameserver.store.ServiceInstance;

/**
 * @author wuyiccc
 * @date 2024/10/20 22:11
 */
public class NodeReplicationMsgEvent extends Event {


    private ServiceInstance serviceInstance;


    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }
}

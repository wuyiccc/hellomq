package com.wuyiccc.hellomq.nameserver.event.model;

import com.wuyiccc.hellomq.nameserver.store.ServiceInstance;

/**
 * @author wuyiccc
 * @date 2024/10/16 23:06
 */
public class ReplicationMsgEvent {

    private ServiceInstance serviceInstance;

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }
}

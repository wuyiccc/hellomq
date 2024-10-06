package com.wuyiccc.hellomq.nameserver.core;

import com.wuyiccc.hellomq.common.constants.BaseConstants;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.store.ServiceInstance;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wuyiccc
 * @date 2024/10/6 22:25
 * <p>
 * 移除非正常服务任务
 */
public class InValidServiceRemoveTask implements Runnable {


    @Override
    public void run() {

        while (true) {
            try {
                TimeUnit.SECONDS.sleep(BaseConstants.SECONDS_3);
                Map<String, ServiceInstance> serviceInstanceMap = CommonCache.getServiceInstanceManager().getServiceInstanceMap();
                long currentTime = System.currentTimeMillis();
                Iterator<String> iterator = serviceInstanceMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String brokerReqId = iterator.next();
                    ServiceInstance serviceInstance = serviceInstanceMap.get(brokerReqId);
                    if (serviceInstance.getLastHeartBeatTime() == null) {
                        // 初次注册, 没有产生心跳, 将注册时间作为最近一次心跳时间
                        if (currentTime - serviceInstance.getFirstRegistryTime() > BaseConstants.MILLISECONDS_9) {
                            // 如果当前时间-注册时间 > 9s, 则说明心跳过期
                            iterator.remove();
                        } else {
                            // 如果没有, 则跳过检查
                            continue;
                        }
                    }
                    if (currentTime - serviceInstance.getLastHeartBeatTime() > BaseConstants.MILLISECONDS_9) {
                        iterator.remove();
                    }
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

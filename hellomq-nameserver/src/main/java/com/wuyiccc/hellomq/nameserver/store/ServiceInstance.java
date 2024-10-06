package com.wuyiccc.hellomq.nameserver.store;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyiccc
 * @date 2024/10/6 10:55
 */
public class ServiceInstance {

    private String brokerIp;

    private Integer brokerPort;

    private Long firstRegistryTime;

    private Long lastHeartBeatTime;

    private Map<String, String> attrs = new HashMap<>();


    public String getBrokerIp() {
        return brokerIp;
    }

    public void setBrokerIp(String brokerIp) {
        this.brokerIp = brokerIp;
    }

    public Integer getBrokerPort() {
        return brokerPort;
    }

    public void setBrokerPort(Integer brokerPort) {
        this.brokerPort = brokerPort;
    }

    public Long getFirstRegistryTime() {
        return firstRegistryTime;
    }

    public void setFirstRegistryTime(Long firstRegistryTime) {
        this.firstRegistryTime = firstRegistryTime;
    }

    public Long getLastHeartBeatTime() {
        return lastHeartBeatTime;
    }

    public void setLastHeartBeatTime(Long lastHeartBeatTime) {
        this.lastHeartBeatTime = lastHeartBeatTime;
    }

    public Map<String, String> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, String> attrs) {
        this.attrs = attrs;
    }
}

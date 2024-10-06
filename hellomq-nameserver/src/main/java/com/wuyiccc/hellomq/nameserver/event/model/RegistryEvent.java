package com.wuyiccc.hellomq.nameserver.event.model;

/**
 * @author wuyiccc
 * @date 2024/10/5 22:45
 *
 * 注册事件
 */
public class RegistryEvent extends Event {


    private String user;

    private String password;

    private String brokerIp;

    private Integer brokerPort;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
}

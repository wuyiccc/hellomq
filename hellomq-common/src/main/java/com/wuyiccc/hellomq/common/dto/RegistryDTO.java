package com.wuyiccc.hellomq.common.dto;

/**
 * @author wuyiccc
 * @date 2024/10/7 17:49
 */
public class RegistryDTO {

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

    @Override
    public String toString() {
        return "RegistryDTO{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", brokerIp='" + brokerIp + '\'' +
                ", brokerPort=" + brokerPort +
                '}';
    }
}

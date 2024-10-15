package com.wuyiccc.hellomq.nameserver.config;

/**
 * @author wuyiccc
 * @date 2024/10/14 22:12
 */
public class NameServerProperties {

    private String nameserverUser;

    private String nameserverPwd;

    private Integer nameserverPort;

    private String replicationMode;

    private TraceReplicationProperties traceReplicationProperties;

    private MasterSlavingReplicationProperties masterSlavingReplicationProperties;

    public String getNameserverUser() {
        return nameserverUser;
    }

    public void setNameserverUser(String nameserverUser) {
        this.nameserverUser = nameserverUser;
    }

    public String getNameserverPwd() {
        return nameserverPwd;
    }

    public void setNameserverPwd(String nameserverPwd) {
        this.nameserverPwd = nameserverPwd;
    }

    public Integer getNameserverPort() {
        return nameserverPort;
    }

    public void setNameserverPort(Integer nameserverPort) {
        this.nameserverPort = nameserverPort;
    }

    public String getReplicationMode() {
        return replicationMode;
    }

    public void setReplicationMode(String replicationMode) {
        this.replicationMode = replicationMode;
    }

    public TraceReplicationProperties getTraceReplicationProperties() {
        return traceReplicationProperties;
    }

    public void setTraceReplicationProperties(TraceReplicationProperties traceReplicationProperties) {
        this.traceReplicationProperties = traceReplicationProperties;
    }

    public MasterSlavingReplicationProperties getMasterSlavingReplicationProperties() {
        return masterSlavingReplicationProperties;
    }

    public void setMasterSlavingReplicationProperties(MasterSlavingReplicationProperties masterSlavingReplicationProperties) {
        this.masterSlavingReplicationProperties = masterSlavingReplicationProperties;
    }
}

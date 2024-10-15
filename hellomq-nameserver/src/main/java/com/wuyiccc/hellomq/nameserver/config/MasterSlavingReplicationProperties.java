package com.wuyiccc.hellomq.nameserver.config;

/**
 * @author wuyiccc
 * @date 2024/10/14 22:26
 */
public class MasterSlavingReplicationProperties {

    private String master;

    private String role;


    private String type;

    private Integer port;


    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}

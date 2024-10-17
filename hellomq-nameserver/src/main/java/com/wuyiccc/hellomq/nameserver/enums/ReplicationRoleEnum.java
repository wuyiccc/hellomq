package com.wuyiccc.hellomq.nameserver.enums;

/**
 * @author wuyiccc
 * @date 2024/10/15 23:07
 */
public enum ReplicationRoleEnum {


    MASTER("master", "主从-主"),
    SLAVE("slave", "主从-从"),
    HEADER("header", "链路复制-头节点"),
    NODE("node", "链路复制-非头节点"),
    ;

    private final String code;

    private final String desc;

    ReplicationRoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ReplicationRoleEnum of(String code) {

        for (ReplicationRoleEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

package com.wuyiccc.hellomq.nameserver.enums;

/**
 * @author wuyiccc
 * @date 2024/10/15 23:07
 */
public enum ReplicationModeEnum {

    MASTER_SLAVE("master_slave", "主从复制模式"),
    TRACE("trace", "链路复制模式");
    ;

    private final String code;

    private final String desc;

    ReplicationModeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ReplicationModeEnum of(String code) {

        for (ReplicationModeEnum value : values()) {
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

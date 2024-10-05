package com.wuyiccc.hellomq.common.enums;

/**
 * @author wuyiccc
 * @date 2024/10/5 22:43
 */
public enum NameServerEventCodeEnum {

    REGISTRY(1, "注册事件"),
    UN_REGISTRY(2, "下线事件"),
    HEART_BEAT(3, "心跳事件"),
    ;

    private int code;

    private String desc;

    NameServerEventCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}

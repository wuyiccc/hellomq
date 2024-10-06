package com.wuyiccc.hellomq.common.enums;

/**
 * @author wuyiccc
 * @date 2024/10/6 10:48
 */
public enum NameServerResponseCodeEnum {
    ERROR_USER_OR_PASSWORD(1001, "账号验证异常"),
    ;


    private int code;

    private String desc;

    NameServerResponseCodeEnum(int code, String desc) {
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

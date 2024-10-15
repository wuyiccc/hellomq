package com.wuyiccc.hellomq.common.utils;

/**
 * @author wuyiccc
 * @date 2024/10/15 23:10
 */
public class AssertUtils {

    public static void isNotBlank(String val, String msg) {

        if (val == null || val.trim().isEmpty()) {
            throw new RuntimeException(msg);
        }
    }

    public static void isNotNull(Object val, String msg) {

        if (val == null) {
            throw new RuntimeException(msg);
        }
    }

    public static void isTrue(Boolean condition, String msg) {

        if (!condition) {
            throw new RuntimeException(msg);
        }
    }
}

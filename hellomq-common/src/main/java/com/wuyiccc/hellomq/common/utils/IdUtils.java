package com.wuyiccc.hellomq.common.utils;

import java.util.UUID;

/**
 * @author wuyiccc
 * @date 2024/10/20 09:24
 */
public class IdUtils {

    public static String generateUUId() {

        return UUID.randomUUID().toString();
    }
}

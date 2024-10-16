package com.wuyiccc.hellomq.nameserver.utils;

import com.wuyiccc.hellomq.nameserver.cache.CommonCache;

/**
 * @author wuyiccc
 * @date 2024/10/16 21:30
 */
public class NameServerUtils {

    public static boolean isVerify(String user, String password) {

        String rightUser = CommonCache.getNameServerProperties().getNameserverUser();
        String rightPassword = CommonCache.getNameServerProperties().getNameserverPwd();
        return rightUser.equals(user) && rightPassword.equals(password);
    }
}

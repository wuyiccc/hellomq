package com.wuyiccc.hellomq.nameserver;

import com.wuyiccc.hellomq.common.constants.NameServerConstants;
import com.wuyiccc.hellomq.nameserver.core.NameServerStarter;

/**
 * @author wuyiccc
 * @date 2024/10/5 16:17
 */
public class NameServerStartUp {

    private static NameServerStarter nameServerStarter;


    public static void main(String[] args) throws InterruptedException {

        nameServerStarter = new NameServerStarter(NameServerConstants.DEFAULT_NAMESERVER_PORT);
        nameServerStarter.startServer();
    }

}

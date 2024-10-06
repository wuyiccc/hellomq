package com.wuyiccc.hellomq.nameserver;

import com.wuyiccc.hellomq.common.constants.NameServerConstants;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.core.InValidServiceRemoveTask;
import com.wuyiccc.hellomq.nameserver.core.NameServerStarter;

import java.io.IOException;

/**
 * @author wuyiccc
 * @date 2024/10/5 16:17
 */
public class NameServerStartUp {

    private static NameServerStarter nameServerStarter;


    public static void main(String[] args) throws InterruptedException, IOException {

        CommonCache.getPropertiesLoader().loadProperties();
        new Thread(new InValidServiceRemoveTask()).start();
        nameServerStarter = new NameServerStarter(NameServerConstants.DEFAULT_NAMESERVER_PORT);
        nameServerStarter.startServer();
    }

}

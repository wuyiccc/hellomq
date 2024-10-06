package com.wuyiccc.hellomq.nameserver;

import com.wuyiccc.hellomq.common.constants.NameServerConstants;
import com.wuyiccc.hellomq.nameserver.cache.CommonCache;
import com.wuyiccc.hellomq.nameserver.core.NameServerStarter;
import com.wuyiccc.hellomq.nameserver.core.PropertiesLoader;

import java.io.IOException;

/**
 * @author wuyiccc
 * @date 2024/10/5 16:17
 */
public class NameServerStartUp {

    private static NameServerStarter nameServerStarter;


    public static void main(String[] args) throws InterruptedException, IOException {


        CommonCache.getPropertiesLoader().loadProperties();

        nameServerStarter = new NameServerStarter(NameServerConstants.DEFAULT_NAMESERVER_PORT);
        nameServerStarter.startServer();
    }

}

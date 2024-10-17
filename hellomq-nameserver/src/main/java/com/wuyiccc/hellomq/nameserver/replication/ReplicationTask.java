package com.wuyiccc.hellomq.nameserver.replication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wuyiccc
 * @date 2024/10/18 00:42
 */
public abstract class ReplicationTask {

    private String taskName;

    private static final Logger log = LoggerFactory.getLogger(ReplicationTask.class);


    public ReplicationTask(String taskName) {
        this.taskName = taskName;
    }

    public void startTaskAsync() {

        Thread task = new Thread(() -> {
            log.info("start job:" + taskName);
            startTask();
        });
        task.setName(taskName);
        task.start();
    }

    abstract void startTask();

}

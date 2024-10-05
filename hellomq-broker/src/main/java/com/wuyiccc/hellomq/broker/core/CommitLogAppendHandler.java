package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.cache.CommonCache;
import com.wuyiccc.hellomq.common.constants.BrokerConstants;
import com.wuyiccc.hellomq.broker.model.CommitLogMessageModel;

import java.io.IOException;
import java.util.Objects;

/**
 * @author wuyiccc
 * @date 2024/8/25 21:44
 */
public class CommitLogAppendHandler {



    public void prepareMMapLoading(String topicName) throws IOException {

        CommitLogMMapFileModel commitLogMMapFileModel = new CommitLogMMapFileModel();
        commitLogMMapFileModel.loadFileInMMap(topicName, 0, BrokerConstants.COMMITLOG_DEFAULT_MMAP_SIZE);
        CommonCache.getCommitLogMMapFileModelManager().put(topicName, commitLogMMapFileModel);
    }

    public void appendMsg(String topic, byte[] content) throws IOException {

        CommitLogMMapFileModel commitLogMMapFileModel = CommonCache.getCommitLogMMapFileModelManager().get(topic);
        if (Objects.isNull(commitLogMMapFileModel)) {
            throw new RuntimeException("topic is invalid");
        }

        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
        commitLogMessageModel.setContent(content);

        commitLogMMapFileModel.writeContent(commitLogMessageModel);
    }


}


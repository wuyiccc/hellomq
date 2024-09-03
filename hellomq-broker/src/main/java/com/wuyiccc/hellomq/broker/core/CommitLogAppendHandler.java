package com.wuyiccc.hellomq.broker.core;

import com.wuyiccc.hellomq.broker.model.CommitLogMessageModel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author wuyiccc
 * @date 2024/8/25 21:44
 */
public class CommitLogAppendHandler {

    private MMapFileModelManager mMapFileModelManager = new MMapFileModelManager();


    public void prepareMMapLoading(String topicName) throws IOException {

        MMapFileModel mMapFileModel = new MMapFileModel();
        mMapFileModel.loadFileInMMap(topicName, 0, 1 * 1024 * 1024);
        this.mMapFileModelManager.put(topicName, mMapFileModel);
    }

    public void appendMsg(String topic, byte[] content) {

        MMapFileModel mMapFileModel = this.mMapFileModelManager.get(topic);
        if (Objects.isNull(mMapFileModel)) {
            throw new RuntimeException("topic is invalid");
        }

        CommitLogMessageModel commitLogMessageModel = new CommitLogMessageModel();
        commitLogMessageModel.setSize(content.length);
        commitLogMessageModel.setContent(content);

        mMapFileModel.writeContent(commitLogMessageModel);
    }

    public void readMsg(String topic) {

        MMapFileModel mMapFileModel = mMapFileModelManager.get(topic);
        if (Objects.isNull(mMapFileModel)) {
            throw new RuntimeException("topic is invalid");
        }
        byte[] content = mMapFileModel.readContent(0, 10);
        System.out.println(new String(content));
    }


}


package com.wuyiccc.hellomq.broker.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyiccc
 * @date 2024/8/25 21:37
 */
public class CommitLogMMapFileModelManager {

    /**
     * key: topic, value: 文件的mmap对象
     */
    private static Map<String, CommitLogMMapFileModel> mMapFileModelMap = new HashMap<>();


    public void put(String topic, CommitLogMMapFileModel commitLogMMapFileModel) {
        mMapFileModelMap.put(topic, commitLogMMapFileModel);
    }

    public CommitLogMMapFileModel get(String topic) {
        return mMapFileModelMap.get(topic);
    }


}

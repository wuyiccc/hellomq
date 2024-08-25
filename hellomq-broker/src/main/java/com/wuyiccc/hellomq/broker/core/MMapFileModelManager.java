package com.wuyiccc.hellomq.broker.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyiccc
 * @date 2024/8/25 21:37
 */
public class MMapFileModelManager {

    /**
     * key: topic, value: 文件的mmap对象
     */
    private static Map<String, MMapFileModel> mMapFileModelMap = new HashMap<>();


    public void put(String topic, MMapFileModel mMapFileModel) {
        mMapFileModelMap.put(topic, mMapFileModel);
    }

    public MMapFileModel get(String topic) {
        return mMapFileModelMap.get(topic);
    }


}

package com.wuyiccc.hellomq.broker.utils;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author wuyiccc
 * @date 2024/8/25 16:07
 */
public class TestMMapUtils {

    private MMapUtils mMapUtils;

    private static final String filePath = "../data/broker/store/test_topic/0000000";

    @Before
    public void setUp() throws IOException {
        mMapUtils = new MMapUtils();
        this.mMapUtils.loadFileInMMap(filePath, 0, 100 * 1024);
    }

    @Test
    public void testLoadFile() throws IOException {

        //this.mapUtils.loadFileInMMap(filePath, 0, 100 * 1024 * 1024);
    }

    @Test
    public void testWriteFile() {
        String str = "this is a test content";
        byte[] content = str.getBytes(StandardCharsets.UTF_8);
        this.mMapUtils.writeContent(content);
        byte[] readContent = this.mMapUtils.readContent(0, content.length + 1);
        System.out.println(new String(readContent));
    }
}

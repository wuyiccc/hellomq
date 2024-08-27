package com.wuyiccc.hellomq.broker.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuyiccc.hellomq.broker.model.HelloMqTopicModel;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.List;

/**
 * @author wuyiccc
 * @date 2024/8/27 22:42
 */
public class FileContentReaderUtils {


    public static String readFromFile(String path) {

        try (BufferedReader in = new BufferedReader(new FileReader(path))) {

            StringBuilder stb = new StringBuilder();
            while (in.ready()) {
                stb.append(in.readLine());
            }

            return stb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {


        String content = FileContentReaderUtils.readFromFile("config/broker/hellomq-topic.json");

        List<HelloMqTopicModel> hello = JSON.parseArray(content, HelloMqTopicModel.class);
        System.out.println(hello);
    }

}

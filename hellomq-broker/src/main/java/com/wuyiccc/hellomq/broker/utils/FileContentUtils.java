package com.wuyiccc.hellomq.broker.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author wuyiccc
 * @date 2024/8/27 22:42
 * <p>
 * 简化版本的文件读写工具
 */
public class FileContentUtils {


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


    public static void overWriteToFile(String path, String content) {
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(content);
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

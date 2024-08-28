package com.wuyiccc.hellomq.broker.utils;

import java.io.BufferedReader;
import java.io.FileReader;

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


}

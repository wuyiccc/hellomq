package com.wuyiccc.hellomq.broker.utils;

/**
 * @author wuyiccc
 * @date 2024/9/3 23:07
 */
public class ByteConvertUtils {

    public static byte[] readInPos(byte[] source, int pos, int len) {

        byte[] result = new byte[len];
        for (int i = pos, j = 0; i < pos + len; i++) {
            result[j++] = source[i];
        }
        return result;
    }


    public static byte[] intToBytes(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value & 0xFF);
        bytes[1] = (byte) ((value >> 8) & 0xFF);
        bytes[2] = (byte) ((value >> 16) & 0xFF);
        bytes[3] = (byte) ((value >> 24) & 0xFF);
        return bytes;
    }


    public static int bytesToInt(byte[] ary) {

        int value;
        value = (ary[0] & 0xFF)
                | ((ary[1] << 8) & 0xFF00)
                | ((ary[2] << 16) & 0XFF0000)
                | ((ary[3] << 24) & 0xFF000000);
        return value;
    }

    public static void main(String[] args) {
        int j = 100;

        byte[] bytes = ByteConvertUtils.intToBytes(j);
        int res = ByteConvertUtils.bytesToInt(bytes);


        System.out.println(bytes.length);
        System.out.println(res);
    }
}

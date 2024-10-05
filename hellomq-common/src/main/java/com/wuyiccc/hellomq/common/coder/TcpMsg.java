package com.wuyiccc.hellomq.common.coder;

/**
 * @author wuyiccc
 * @date 2024/10/5 14:44
 */
public class TcpMsg {

    /**
     * 魔数
     */
    private short magic;


    /**
     * 表示请求包的具体含义
     */
    private int code;


    private int len;

    private byte[] body;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}


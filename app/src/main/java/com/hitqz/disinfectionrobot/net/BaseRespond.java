package com.hitqz.disinfectionrobot.net;

/**
 * 网络请求结果 基类,剥离出数据给上层
 */
public class BaseRespond<T> {

    private int resultCode;
    private String msg;
    private T data;

    public int getCode() {
        return resultCode;
    }

    public void setCode(int code) {
        this.resultCode = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

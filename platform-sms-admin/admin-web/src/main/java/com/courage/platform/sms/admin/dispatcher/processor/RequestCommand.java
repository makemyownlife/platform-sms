package com.courage.platform.sms.admin.dispatcher.processor;

/**
 * 处理机接收到的请求命令
 * Created by zhangyong on 2023/7/14.
 */
public class RequestCommand<T> {

    public RequestCommand(T t) {
        this.data = t;
    }

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}


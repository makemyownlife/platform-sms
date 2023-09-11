package com.courage.platform.sms.admin.dispatcher.processor.requeset;

/**
 * 处理机接收到的请求命令
 * Created by zhangyong on 2023/7/14.
 */
public class RequestEntity<T> {

    public RequestEntity(T t) {
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


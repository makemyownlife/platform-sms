package com.courage.platform.sms.admin.loader.processor;

/**
 * 处理机接收到的请求命令
 * Created by zhangyong on 2023/7/14.
 */
public class ProcessorRequest<T> {

    private T data;

    public ProcessorRequest(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}


package com.courage.platform.sms.admin.loader.processor;

/**
 * 请求命令枚举
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


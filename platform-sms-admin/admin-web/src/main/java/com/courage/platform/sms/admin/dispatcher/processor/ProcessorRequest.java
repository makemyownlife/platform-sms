package com.courage.platform.sms.admin.dispatcher.processor;

/**
 * 处理机接收到的请求命令
 * Created by zhangyong on 2023/7/14.
 */
public class ProcessorRequest<ProcessorRequestBody> {

    private ProcessorRequestBody data;

    public ProcessorRequest(ProcessorRequestBody data) {
        this.data = data;
    }

    public ProcessorRequestBody getData() {
        return data;
    }

}


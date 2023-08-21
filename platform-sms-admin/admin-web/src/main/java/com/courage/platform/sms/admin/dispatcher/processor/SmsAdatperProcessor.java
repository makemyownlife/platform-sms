package com.courage.platform.sms.admin.dispatcher.processor;

/**
 * 适配器处理器接口
 * Created by zhangyong on 2023/5/5.
 */
public interface SmsAdatperProcessor<T, R> {

    ResponseCommand<R> processRequest(RequestCommand<T> processorRequest);

}

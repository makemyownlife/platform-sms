package com.courage.platform.sms.admin.dispatcher;

import com.courage.platform.sms.admin.dispatcher.processor.ProcessorRequest;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorResponse;

/**
 * 适配器处理器接口
 * Created by zhangyong on 2023/5/5.
 */
public interface SmsAdatperProcessor<T, R> {

    ProcessorResponse<R> processRequest(ProcessorRequest<T> processorRequest);

}

package com.courage.platform.sms.admin.loader;

import com.courage.platform.sms.admin.loader.processors.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processors.ProcessorResponse;

/**
 * 适配器处理器接口
 * Created by zhangyong on 2023/5/5.
 */
public interface SmsAdatperProcessor<P, T> {

    ProcessorResponse<T> processRequest(ProcessorRequest<P> processorRequest);
    
}

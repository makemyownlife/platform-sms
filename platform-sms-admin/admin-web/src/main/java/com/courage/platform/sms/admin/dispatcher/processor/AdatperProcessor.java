package com.courage.platform.sms.admin.dispatcher.processor;

import com.courage.platform.sms.admin.common.utils.ResponseEntity;
import com.courage.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
/**
 * 适配器处理器接口
 * Created by zhangyong on 2023/5/5.
 */
public interface AdatperProcessor<T, R> {

    ResponseEntity<R> processRequest(RequestEntity<T> processorRequest);

}

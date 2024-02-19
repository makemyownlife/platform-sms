package cn.javayong.platform.sms.admin.dispatcher.processor;

import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
import cn.javayong.platform.sms.admin.common.utils.ResponseEntity;

/**
 * 适配器处理器接口
 * Created by zhangyong on 2023/5/5.
 */
public interface AdatperProcessor<T, R> {

    ResponseEntity<R> processRequest(RequestEntity<T> processorRequest);

}

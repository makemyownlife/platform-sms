package com.courage.platform.sms.admin.loader.processors.impl;

import com.courage.platform.sms.admin.loader.SmsAdatperProcessor;
import com.courage.platform.sms.admin.loader.processors.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processors.ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 发送短信处理器
 * Created by zhangyong on 2023/7/14.
 */
@Component
public class SendMessageRequestProcessor implements SmsAdatperProcessor {

    private static Logger logger = LoggerFactory.getLogger(SendMessageRequestProcessor.class);

    @Override
    public ProcessorResponse processRequest(ProcessorRequest processorRequest) {
        return null;
    }

}

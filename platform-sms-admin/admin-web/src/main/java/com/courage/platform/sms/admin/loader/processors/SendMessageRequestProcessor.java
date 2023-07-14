package com.courage.platform.sms.admin.loader.processors;

import com.courage.platform.sms.adapter.command.SmsRequestCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;
import com.courage.platform.sms.admin.loader.SmsAdatperProcessor;
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
    public SmsResponseCommand processRequest(SmsRequestCommand requestCommand) {
        return null;
    }

}

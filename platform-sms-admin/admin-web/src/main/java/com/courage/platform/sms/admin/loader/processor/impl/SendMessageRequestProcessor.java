package com.courage.platform.sms.admin.loader.processor.impl;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import com.courage.platform.sms.admin.dao.TSmsTemplateDAO;
import com.courage.platform.sms.admin.dao.domain.TSmsTemplate;
import com.courage.platform.sms.admin.loader.SmsAdapterLoader;
import com.courage.platform.sms.admin.loader.SmsAdatperProcessor;
import com.courage.platform.sms.admin.loader.processor.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processor.ProcessorResponse;
import com.courage.platform.sms.admin.loader.processor.body.SendMessageRequestBody;
import com.courage.platform.sms.client.SmsSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 发送短信处理器
 * Created by zhangyong on 2023/7/14.
 */
@Component
public class SendMessageRequestProcessor implements SmsAdatperProcessor<SendMessageRequestBody, SmsSenderResult> {

    private static Logger logger = LoggerFactory.getLogger(SendMessageRequestProcessor.class);

    @Autowired
    private SmsAdapterLoader smsAdapterLoader;

    @Autowired
    private TSmsTemplateBindingDAO bindingDAO;

    @Autowired
    private TSmsTemplateDAO templateDAO;

    @Override
    public ProcessorResponse<SmsSenderResult> processRequest(ProcessorRequest<SendMessageRequestBody> processorRequest) {
        SendMessageRequestBody param = processorRequest.getData();
        logger.info("开始处理处理短信请求，参数:" + JSON.toJSONString(param));
        String templateId = param.getTemplateId();
        TSmsTemplate tSmsTemplate = templateDAO.selectByPrimaryKey(Long.valueOf(templateId));

        SmsSenderResult smsSenderResult = new SmsSenderResult(null);
        return null;
    }

}

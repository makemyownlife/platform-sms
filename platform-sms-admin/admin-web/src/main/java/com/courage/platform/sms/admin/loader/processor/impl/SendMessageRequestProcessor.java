package com.courage.platform.sms.admin.loader.processor.impl;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import com.courage.platform.sms.admin.dao.domain.TSmsTemplateBinding;
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

import java.util.List;

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

    @Override
    public ProcessorResponse processRequest(ProcessorRequest<SendMessageRequestBody> processorRequest) {
        SendMessageRequestBody param = processorRequest.getData();
        logger.info("开始处理处理短信请求，参数:" + JSON.toJSONString(param));
        String appId = param.getAppId();
        String templateId = param.getTemplateId();
        String mobile = param.getMobile();
        String templateParam = param.getTemplateParam();

        // 查询该模版下的绑定渠道
        List<TSmsTemplateBinding> bindingList = bindingDAO.selectBindingsByTemplateId(Long.valueOf(templateId));

        SmsSenderResult smsSenderResult = new SmsSenderResult(null);
        return null;
    }

}

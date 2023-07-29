package com.courage.platform.sms.admin.loader.processor.impl;

import com.courage.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import com.courage.platform.sms.admin.domain.TSmsTemplateBinding;
import com.courage.platform.sms.admin.loader.SmsAdapterLoader;
import com.courage.platform.sms.admin.loader.SmsAdatperProcessor;
import com.courage.platform.sms.admin.loader.processor.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processor.ProcessorResponse;
import com.courage.platform.sms.client.SmsSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 发送短信处理器
 * Created by zhangyong on 2023/7/14.
 */
@Component
public class SendMessageRequestProcessor implements SmsAdatperProcessor<Map<String, String>, SmsSenderResult> {

    private static Logger logger = LoggerFactory.getLogger(SendMessageRequestProcessor.class);

    @Autowired
    private SmsAdapterLoader smsAdapterLoader;

    @Autowired
    private TSmsTemplateBindingDAO bindingDAO;

    @Override
    public ProcessorResponse processRequest(ProcessorRequest<Map<String, String>> processorRequest) {
        Map<String, String> param = processorRequest.getData();
        logger.info("开始处理处理短信请求，参数:" + param);
        String appKey = param.get("appKey");
        String templateId = param.get("templateId");
        String mobile = param.get("mobile");
        String templateParam = param.get("templateParam");

        // 查询该模版下的绑定渠道
        List<TSmsTemplateBinding> bindingList = bindingDAO.selectBindingsByTemplateId(Long.valueOf(templateId));
        SmsSenderResult smsSenderResult = new SmsSenderResult(null);
        return null;
    }

}

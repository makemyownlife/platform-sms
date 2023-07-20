package com.courage.platform.sms.admin.service;


import com.courage.platform.sms.admin.controller.model.BaseModel;
import com.courage.platform.sms.admin.domain.TSmsTemplate;

import java.util.List;
import java.util.Map;

public interface SmsTemplateService {

    List<TSmsTemplate> queryTemplates(Map<String, Object> params);

    Long queryCountTemplates(Map<String, Object> param);

    BaseModel addSmsTemplate(TSmsTemplate tSmsTemplate);

    BaseModel updateSmsTemplate(TSmsTemplate tSmsTemplate);

    BaseModel deleteSmsTemplate(Long id);

    BaseModel autoBindChannel(String channelIds, Long templateId);

}

package cn.javayong.platform.sms.admin.service;

import cn.javayong.platform.sms.admin.common.utils.ResponseEntity;
import cn.javayong.platform.sms.admin.domain.TSmsTemplate;

import java.util.List;
import java.util.Map;

public interface SmsTemplateService {

    List<TSmsTemplate> queryTemplates(Map<String, Object> params);

    Long queryCountTemplates(Map<String, Object> param);

    ResponseEntity<String> addSmsTemplate(TSmsTemplate tSmsTemplate);

    ResponseEntity<String> updateSmsTemplate(TSmsTemplate tSmsTemplate);

    ResponseEntity<String> deleteSmsTemplate(Long id);

    ResponseEntity<String> autoBindChannel(String channelIds, Long templateId);

    ResponseEntity<String> handBindChannel(String channelIds, Long templateId, String templateCode);

}

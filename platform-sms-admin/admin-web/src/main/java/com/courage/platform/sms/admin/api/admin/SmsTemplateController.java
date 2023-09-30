package com.courage.platform.sms.admin.api.admin;

import com.courage.platform.sms.admin.dispatcher.processor.response.ResponseEntity;
import com.courage.platform.sms.admin.domain.vo.BaseModel;
import com.courage.platform.sms.admin.domain.vo.Pager;
import com.courage.platform.sms.admin.domain.TSmsTemplate;
import com.courage.platform.sms.admin.service.SmsTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsTemplateController {

    private final static Logger logger = LoggerFactory.getLogger(SmsTemplateController.class);

    @Autowired
    private SmsTemplateService smsTemplateService;

    @PostMapping(value = "/templates")
    public ResponseEntity<Pager> templates(String templateName, String signName, String page, String size) {
        Map<String, Object> param = new HashMap<>();
        if (StringUtils.isNotEmpty(templateName)) {
            param.put("templateName", StringUtils.trimToEmpty(templateName));
        }
        if (StringUtils.isNotEmpty(signName)) {
            param.put("signName", signName);
        }
        param.put("page", page == null ? 1 : Integer.valueOf(page));
        param.put("size", size == null ? 10 : Integer.valueOf(size));
        List<TSmsTemplate> tSmsTemplateList = smsTemplateService.queryTemplates(param);
        Long count = smsTemplateService.queryCountTemplates(param);
        Pager pager = new Pager();
        pager.setCount(count);
        pager.setItems(tSmsTemplateList);
        return ResponseEntity.success(pager);
    }

    @PostMapping(value = "/addSmsTemplate")
    public ResponseEntity<String> addSmsTemplate(@RequestBody TSmsTemplate tSmsTemplate) {
        return smsTemplateService.addSmsTemplate(tSmsTemplate);
    }

    @PostMapping(value = "/updateSmsTemplate")
    public ResponseEntity<String> updateSmsTemplate(@RequestBody TSmsTemplate tSmsTemplate) {
        return smsTemplateService.updateSmsTemplate(tSmsTemplate);
    }

    @PostMapping(value = "/deleteSmsTemplate")
    public ResponseEntity<String> deleteSmsTemplate(String id) {
        return smsTemplateService.deleteSmsTemplate(Long.valueOf(id));
    }

    @PostMapping(value = "/autoBindChannel")
    public ResponseEntity<String> autoBindChannel(String channelIds, Long templateId, String templateCode) {
        if (StringUtils.isNotEmpty(templateCode)) {
            return smsTemplateService.handBindChannel(channelIds, templateId, templateCode);
        } else {
            return smsTemplateService.autoBindChannel(channelIds, templateId);
        }
    }

}


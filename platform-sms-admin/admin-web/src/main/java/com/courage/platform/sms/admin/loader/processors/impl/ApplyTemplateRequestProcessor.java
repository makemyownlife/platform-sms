package com.courage.platform.sms.admin.loader.processors.impl;

import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;
import com.courage.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import com.courage.platform.sms.admin.dao.TSmsTemplateDAO;
import com.courage.platform.sms.admin.domain.TSmsTemplate;
import com.courage.platform.sms.admin.domain.TSmsTemplateBinding;
import com.courage.platform.sms.admin.loader.SmsAdapterLoader;
import com.courage.platform.sms.admin.loader.SmsAdatperProcessor;
import com.courage.platform.sms.admin.loader.processors.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processors.ProcessorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * admin后台向渠道申请模版
 * Created by zhangyong on 2023/7/14.
 */
@Component
public class ApplyTemplateRequestProcessor implements SmsAdatperProcessor {

    private static Logger logger = LoggerFactory.getLogger(ApplyTemplateRequestProcessor.class);

    @Autowired
    private SmsAdapterLoader smsAdapterLoader;

    @Autowired
    private TSmsTemplateDAO tSmsTemplateDAO;

    @Autowired
    private TSmsTemplateBindingDAO tSmsTemplateBindingDAO;

    @Override
    public ProcessorResponse processRequest(ProcessorRequest processorRequest) {
        Long templateId = (Long) processorRequest.getData();
        TSmsTemplate tSmsTemplate = tSmsTemplateDAO.selectByPrimaryKey(templateId);
        if (tSmsTemplate != null) {
            List<TSmsTemplateBinding> bindings = tSmsTemplateBindingDAO.selectBindingsByTemplateId(templateId);
            for (TSmsTemplateBinding binding : bindings) {
                OuterAdapter outerAdapter = smsAdapterLoader.getAdapterByChannelId(binding.getChannelId().intValue());
                if (outerAdapter != null) {
                    AddSmsTemplateCommand addSmsTemplateCommand = new AddSmsTemplateCommand();
                    addSmsTemplateCommand.setTemplateName(tSmsTemplate.getTemplateName());
                    addSmsTemplateCommand.setTemplateContent(tSmsTemplate.getContent());
                    addSmsTemplateCommand.setRemark("你好");
                    addSmsTemplateCommand.setTemplateType(tSmsTemplate.getTemplateType());
                    SmsResponseCommand smsResponseCommand = outerAdapter.addSmsTemplate(addSmsTemplateCommand);
                    if (smsResponseCommand.getCode() == SmsResponseCommand.SUCCESS_CODE) {
                        String templateCode = (String) smsResponseCommand.getData();
                        binding.setTemplateCode(templateCode);
                        binding.setStatus((byte) 1);            // 0 : 待提交 1：待审核  2：审核成功 3：审核失败
                        tSmsTemplateBindingDAO.updateByPrimaryKeySelective(binding);
                    }
                }
            }
        }
        return null;
    }

}

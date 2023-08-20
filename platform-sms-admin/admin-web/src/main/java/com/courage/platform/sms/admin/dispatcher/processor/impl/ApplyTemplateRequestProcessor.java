package com.courage.platform.sms.admin.dispatcher.processor.impl;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;
import com.courage.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import com.courage.platform.sms.admin.dao.TSmsTemplateDAO;
import com.courage.platform.sms.admin.dao.domain.TSmsTemplate;
import com.courage.platform.sms.admin.dao.domain.TSmsTemplateBinding;
import com.courage.platform.sms.admin.dispatcher.SmsAdapterLoader;
import com.courage.platform.sms.admin.dispatcher.SmsAdatperProcessor;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorRequest;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorRequestBody;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorResponse;
import com.courage.platform.sms.admin.dispatcher.processor.body.ApplyTemplateRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * admin 后台向渠道申请模版
 * Created by zhangyong on 2023/7/14.
 */
@Component
public class ApplyTemplateRequestProcessor implements SmsAdatperProcessor<ApplyTemplateRequestBody, String> {

    private static Logger logger = LoggerFactory.getLogger(ApplyTemplateRequestProcessor.class);

    @Autowired
    private SmsAdapterLoader smsAdapterLoader;

    @Autowired
    private TSmsTemplateDAO tSmsTemplateDAO;

    @Autowired
    private TSmsTemplateBindingDAO tSmsTemplateBindingDAO;

    @Override
    public ProcessorResponse<String> processRequest(ProcessorRequest<ApplyTemplateRequestBody> processorRequest) {
        ApplyTemplateRequestBody requestBody = processorRequest.getData();
        Long bindingId = requestBody.getBindingId();
        TSmsTemplateBinding binding = tSmsTemplateBindingDAO.selectByPrimaryKey(bindingId);
        if (binding != null) {
            if (binding.getStatus() == 0) { // 待提交
                TSmsTemplate tSmsTemplate = tSmsTemplateDAO.selectByPrimaryKey(binding.getTemplateId());
                OuterAdapter outerAdapter = smsAdapterLoader.getAdapterByChannelId(binding.getChannelId().intValue());
                if (outerAdapter != null) {
                    AddSmsTemplateCommand addSmsTemplateCommand = new AddSmsTemplateCommand();
                    addSmsTemplateCommand.setTemplateName(tSmsTemplate.getTemplateName());
                    addSmsTemplateCommand.setTemplateContent(tSmsTemplate.getContent());
                    addSmsTemplateCommand.setRemark(tSmsTemplate.getRemark());
                    addSmsTemplateCommand.setTemplateType(tSmsTemplate.getTemplateType());
                    logger.info("开始向渠道：" + binding.getChannelId() + " 申请添加模版 请求内容：" + JSON.toJSONString(addSmsTemplateCommand));
                    SmsResponseCommand<Map<String, String>> smsResponseCommand = outerAdapter.addSmsTemplate(addSmsTemplateCommand);
                    logger.info("结束向渠道：" + binding.getChannelId() + " 申请添加模版 响应结果：" + JSON.toJSONString(smsResponseCommand));
                    if (smsResponseCommand.getCode() == SmsResponseCommand.SUCCESS_CODE) {
                        Map<String, String> bodyMap = smsResponseCommand.getData();
                        String templateCode = bodyMap.get("templateCode");
                        String templateContent = bodyMap.get("templateContent");
                        Integer status = bodyMap.get("status") == null ? (byte) 1 : Integer.valueOf(bodyMap.get("status"));
                        binding.setTemplateCode(templateCode);
                        binding.setTemplateContent(templateContent);
                        binding.setStatus(status);                                  // 0 : 待提交 1：待审核  2：审核成功 3：审核失败
                        tSmsTemplateBindingDAO.updateByPrimaryKeySelective(binding);
                        return ProcessorResponse.success(templateCode);
                    }
                }
            }
        }
        return ProcessorResponse.fail("绑定失败");
    }
}
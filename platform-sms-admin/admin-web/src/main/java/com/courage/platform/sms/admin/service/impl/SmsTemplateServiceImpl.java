package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.config.IdGenerator;
import com.courage.platform.sms.admin.controller.model.BaseModel;
import com.courage.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import com.courage.platform.sms.admin.dao.TSmsTemplateDAO;
import com.courage.platform.sms.admin.domain.TSmsTemplate;
import com.courage.platform.sms.admin.domain.TSmsTemplateBinding;
import com.courage.platform.sms.admin.loader.SmsAdapterService;
import com.courage.platform.sms.admin.loader.processors.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processors.ProcessorRequestCode;
import com.courage.platform.sms.admin.service.SmsTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {

    private final static Logger logger = LoggerFactory.getLogger(SmsTemplateServiceImpl.class);

    @Autowired
    private TSmsTemplateDAO tSmsTemplateDAO;

    @Autowired
    private TSmsTemplateBindingDAO tSmsTemplateBindingDAO;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private SmsAdapterService smsAdapterService;

    @Override
    public List<TSmsTemplate> queryTemplates(Map<String, Object> params) {
        return tSmsTemplateDAO.queryTemplates(params);
    }

    @Override
    public Long queryCountTemplates(Map<String, Object> param) {
        return tSmsTemplateDAO.queryCountTemplates(param);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseModel addSmsTemplate(TSmsTemplate tSmsTemplate) {
        Long templateId = idGenerator.createUniqueId(tSmsTemplate.getSignName());
        try {
            tSmsTemplate.setId(templateId);
            tSmsTemplate.setCreateTime(new Date());
            tSmsTemplate.setUpdateTime(new Date());
            tSmsTemplate.setStatus((byte) 0);
            tSmsTemplateDAO.insert(tSmsTemplate);
            Long[] channelIds = tSmsTemplate.getChannelIds();
            for (Long channelId : channelIds) {
                Long bindingId = idGenerator.createUniqueId(tSmsTemplate.getSignName());
                TSmsTemplateBinding binding = new TSmsTemplateBinding();
                binding.setId(bindingId);
                binding.setTemplateId(tSmsTemplate.getId());
                binding.setChannelId(channelId);
                // 0 : 待提交 1：待审核  2：审核成功 3：审核失败
                binding.setStatus((byte) 0);
                binding.setTemplateContent(StringUtils.EMPTY);
                binding.setTemplateCode(StringUtils.EMPTY);
                binding.setCreateTime(new Date());
                binding.setUpdateTime(new Date());
                tSmsTemplateBindingDAO.insertSelective(binding);
            }
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("addSmsTemplate error:", e);
            return BaseModel.getInstance("fail");
        } finally {
            smsAdapterService.processRequest(ProcessorRequestCode.APPLY_TEMPLATE, new ProcessorRequest(templateId));
        }
    }

    @Override
    public BaseModel updateSmsTemplate(TSmsTemplate tSmsTemplate) {
        try {
            tSmsTemplate.setUpdateTime(new Date());
            tSmsTemplateDAO.updateByPrimaryKeySelective(tSmsTemplate);
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("updateSmsTemplate error:", e);
            return BaseModel.getInstance("fail");
        }
    }

    @Override
    public BaseModel deleteSmsTemplate(Long id) {
        try {
            tSmsTemplateDAO.deleteByPrimaryKey(id);
            tSmsTemplateBindingDAO.deleteTemplateBindingByTemplateId(id);
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("deleteSmsTemplate error:", e);
            return BaseModel.getInstance("fail");
        }
    }

    @Override
    public BaseModel getSmsTemplatesBinding(Long id) {
        return null;
    }

}


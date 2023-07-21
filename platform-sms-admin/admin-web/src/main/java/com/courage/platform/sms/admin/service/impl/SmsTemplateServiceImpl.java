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
        List<TSmsTemplate> tSmsTemplates = tSmsTemplateDAO.queryTemplates(params);
        for (TSmsTemplate tSmsTemplate : tSmsTemplates) {
            List<TSmsTemplateBinding> bindingList = tSmsTemplateBindingDAO.selectBindingsByTemplateId(tSmsTemplate.getId());
            tSmsTemplate.setBindingList(bindingList);
        }
        return tSmsTemplates;
    }

    @Override
    public Long queryCountTemplates(Map<String, Object> param) {
        return tSmsTemplateDAO.queryCountTemplates(param);
    }

    @Override
    public BaseModel addSmsTemplate(TSmsTemplate tSmsTemplate) {
        Long templateId = idGenerator.createUniqueId(tSmsTemplate.getSignName());
        try {
            tSmsTemplate.setId(templateId);
            tSmsTemplate.setCreateTime(new Date());
            tSmsTemplate.setUpdateTime(new Date());
            tSmsTemplate.setStatus((byte) 0);
            tSmsTemplateDAO.insert(tSmsTemplate);
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("addSmsTemplate error:", e);
            return BaseModel.getInstance("fail");
        } finally {
            // smsAdapterService.processRequest(ProcessorRequestCode.APPLY_TEMPLATE, new ProcessorRequest(templateId));
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
    public BaseModel autoBindChannel(String channelIds, Long templateId) {
        try {
            String[] channelIdsArr = StringUtils.split(channelIds, ',');
            for (String channelId : channelIdsArr) {
                //先查询是否已经提交过
                TSmsTemplateBinding binding = tSmsTemplateBindingDAO.selectBindingByTemplateIdAndChannelId(templateId, Long.valueOf(channelId));
                if (binding == null) {
                    Long bindingId = idGenerator.createUniqueId(String.valueOf(templateId));
                    binding = new TSmsTemplateBinding();
                    binding.setId(bindingId);
                    binding.setTemplateId(templateId);
                    binding.setChannelId(Long.valueOf(channelId));
                    // 0 : 待提交 1：待审核  2：审核成功 3：审核失败
                    binding.setStatus((byte) 0);
                    binding.setTemplateContent(StringUtils.EMPTY);
                    binding.setTemplateCode(StringUtils.EMPTY);
                    binding.setCreateTime(new Date());
                    binding.setUpdateTime(new Date());
                    tSmsTemplateBindingDAO.insertSelective(binding);
                    // 向渠道申请模版
                }
                smsAdapterService.processRequest(ProcessorRequestCode.APPLY_TEMPLATE, new ProcessorRequest(binding.getId()));
            }
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("autoBindChannel error:", e);
            return BaseModel.getInstance("fail");
        }
    }

}


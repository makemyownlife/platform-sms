package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.model.BaseModel;
import com.courage.platform.sms.admin.service.SmsTemplateService;
import com.courage.platform.sms.dao.TSmsTemplateDAO;
import com.courage.platform.sms.domain.TSmsTemplate;
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

    @Override
    public List<TSmsTemplate> queryTemplates(Map<String, Object> params) {
        return tSmsTemplateDAO.queryTemplates(params);
    }

    @Override
    public Long queryCountTemplates(Map<String, Object> param) {
        return tSmsTemplateDAO.queryCountTemplates(param);
    }

    @Override
    public BaseModel addSmsTemplate(TSmsTemplate tSmsTemplate) {
        try {
            tSmsTemplate.setCreateTime(new Date());
            tSmsTemplate.setUpdateTime(new Date());
            tSmsTemplate.setStatus((byte) 0);
            tSmsTemplateDAO.insert(tSmsTemplate);
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("addSmsTemplate error:", e);
            return BaseModel.getInstance("fail");
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
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("deleteSmsTemplate error:", e);
            return BaseModel.getInstance("fail");
        }
    }
}


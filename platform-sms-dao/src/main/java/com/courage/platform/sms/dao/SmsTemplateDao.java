package com.courage.platform.sms.dao;

import com.courage.platform.sms.dao.domain.SmsTemplate;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface SmsTemplateDao {

    SmsTemplate findSmsTemplateById(@Param("id") Integer id);

    List<SmsTemplate> findAll();

    List<SmsTemplate> findSmsTemplateByParams(Map params);

    int findCountByParams(Map params);

    void saveSmsTemplate(SmsTemplate smsTemplate);

    void updateSmsTemplate(SmsTemplate smsTemplate);

    void deleteSmsTemplateByIds(@Param("ids") String[] ids);

}

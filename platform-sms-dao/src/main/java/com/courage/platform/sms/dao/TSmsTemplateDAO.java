package com.courage.platform.sms.dao;

import com.courage.platform.sms.domain.TSmsTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * TSmsTemplateDAO继承基类
 */
@Repository
public interface TSmsTemplateDAO extends MyBatisBaseDao<TSmsTemplate, Long> {

    List<TSmsTemplate> queryTemplates(Map<String, Object> param);

    Long queryCountTemplates(Map<String, Object> param);

}
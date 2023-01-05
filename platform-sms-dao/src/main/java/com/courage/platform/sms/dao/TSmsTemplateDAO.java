package com.courage.platform.sms.dao;

import com.courage.platform.sms.domain.TSmsTemplate;
import org.springframework.stereotype.Repository;

/**
 * TSmsTemplateDAO继承基类
 */
@Repository
public interface TSmsTemplateDAO extends MyBatisBaseDao<TSmsTemplate, Long> {
}
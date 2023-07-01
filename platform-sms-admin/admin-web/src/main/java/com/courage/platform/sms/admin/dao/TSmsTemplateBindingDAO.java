package com.courage.platform.sms.admin.dao;

import com.courage.platform.sms.admin.domain.TSmsTemplateBinding;
import org.springframework.stereotype.Repository;

/**
 * TSmsTemplateBindingDAO继承基类
 */
@Repository
public interface TSmsTemplateBindingDAO extends MyBatisBaseDao<TSmsTemplateBinding, Long> {

    Integer deleteTemplateBindingByTemplateId(Long templateId);

}
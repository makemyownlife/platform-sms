package cn.javayong.platform.sms.admin.dao;

import cn.javayong.platform.sms.admin.domain.TSmsTemplate;
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
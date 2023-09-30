package com.courage.platform.sms.admin.dao;

import com.courage.platform.sms.admin.domain.TSmsTemplateBinding;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  TSmsTemplateBindingDAO 继承基类
 */
@Repository
public interface TSmsTemplateBindingDAO extends MyBatisBaseDao<TSmsTemplateBinding, Long> {

    Integer deleteTemplateBindingByTemplateId(Long templateId);

    Integer selectCountByChannelId(Integer channelId);

    List<TSmsTemplateBinding> selectBindingsByTemplateId(Long templateId);

    TSmsTemplateBinding selectBindingByTemplateIdAndChannelId(@Param("templateId") Long templateId , @Param("channelId") Long channelId);

}
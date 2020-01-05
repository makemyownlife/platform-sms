package com.courage.platform.sms.dao;

import com.courage.platform.sms.dao.domain.SmsRestrict;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SmsRestrictDao {

    int findCountByParams(Map params);

    SmsRestrict findSmsRestrictById(@Param("id") Integer id);

    List<SmsRestrict> findAll();

    List<SmsRestrict> findSmsRestrictByParams(Map params);

    int insertRestrict(SmsRestrict restrict);

    int updateRestrict(SmsRestrict restrict);

    void deleteRestrictByIds(@Param("ids") String[] ids);

}

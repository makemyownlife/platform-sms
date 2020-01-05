package com.courage.platform.sms.dao;

import com.courage.platform.sms.dao.domain.SmsAppinfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SmsAppinfoDao {

    SmsAppinfo findSmsAppinfoByAppKey(@Param("appId") String appKey);

    List<SmsAppinfo> findAll();

    int insertAppInfo(SmsAppinfo appInfo);

    int updateAppInfo(SmsAppinfo appInfo);

}

package com.courage.platform.sms.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/9.
 */
@Repository
public interface SmsRecordDao {

    //保存短信记录
    void addSmsRecord(@Param("params") HashMap map);

    //保存短信详情
    void addSmsRecordDetail(@Param("msgIds") Map msgIds, @Param("smsMessage") HashMap smsMessage);

    //获取符合条件的记录总数
    int getTotalSmsRecordDetail(@Param("params") HashMap map);

    //获取所有app_name、app_id
    List<Map<String, String>> getAppNames();
}

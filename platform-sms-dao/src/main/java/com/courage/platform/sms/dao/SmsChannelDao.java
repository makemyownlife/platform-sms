package com.courage.platform.sms.dao;

import com.courage.platform.sms.dao.domain.SmsChannel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SmsChannelDao {

    List<SmsChannel> findAllChannel();

    List<SmsChannel> findUsedChannel();

    SmsChannel findChannelById(Integer id);

    List<SmsChannel> findSmsChannelByParams(Map params);

    int findCountByParams(Map params);

    void saveSmsChannel(SmsChannel smsChannel);

    void updateSmsChannel(SmsChannel smsChannel);

    void deleteSmsChannelByIds(@Param("ids") String[] ids);

}

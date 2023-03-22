package com.courage.platform.sms.dao;

import com.courage.platform.sms.domain.TSmsChannel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TSmsChannelDAO extends MyBatisBaseDao<TSmsChannel, Integer> {

    List<TSmsChannel> queryChannels(Map<String, String> param);

    TSmsChannel queryChannelByAppkeyAndChannelType(@Param("channelAppkey") String appkey, @Param("channelType") String channelType);

}
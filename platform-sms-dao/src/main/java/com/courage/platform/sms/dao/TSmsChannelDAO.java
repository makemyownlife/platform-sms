package com.courage.platform.sms.dao;

import com.courage.platform.sms.domain.TSmsChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "tSmsChannelDAO")
public interface TSmsChannelDAO extends MyBatisBaseDao<TSmsChannel, Integer> {

    List<TSmsChannel> queryChannels();

}
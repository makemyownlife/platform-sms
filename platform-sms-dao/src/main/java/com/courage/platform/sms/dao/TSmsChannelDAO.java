package com.courage.platform.sms.dao;

import com.courage.platform.sms.domain.TSmsChannel;
import org.springframework.stereotype.Repository;

/**
 * TSmsChannelDAO继承基类
 */
@Repository
public interface TSmsChannelDAO extends MyBatisBaseDao<TSmsChannel, Integer> {
}
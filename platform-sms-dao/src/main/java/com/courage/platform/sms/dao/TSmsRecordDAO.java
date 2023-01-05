package com.courage.platform.sms.dao;

import com.courage.platform.sms.domain.TSmsRecord;
import org.springframework.stereotype.Repository;

/**
 * TSmsRecordDAO继承基类
 */
@Repository
public interface TSmsRecordDAO extends MyBatisBaseDao<TSmsRecord, Long> {
}
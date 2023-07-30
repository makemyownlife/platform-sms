package com.courage.platform.sms.admin.dao;

import com.courage.platform.sms.admin.dao.domain.TSmsRecord;
import org.springframework.stereotype.Repository;

/**
 * TSmsRecordDAO继承基类
 */
@Repository
public interface TSmsRecordDAO extends MyBatisBaseDao<TSmsRecord, Long> {

    
}
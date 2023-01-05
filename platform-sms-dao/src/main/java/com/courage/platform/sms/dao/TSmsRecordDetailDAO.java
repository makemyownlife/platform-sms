package com.courage.platform.sms.dao;

import com.courage.platform.sms.domain.TSmsRecordDetail;
import org.springframework.stereotype.Repository;

/**
 * TSmsRecordDetailDAO继承基类
 */
@Repository
public interface TSmsRecordDetailDAO extends MyBatisBaseDao<TSmsRecordDetail, Long> {
}
package com.courage.platform.sms.admin.dao;

import com.courage.platform.sms.admin.domain.TSmsRecordDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * TSmsRecordDetailDAO继承基类
 */
@Repository
public interface TSmsRecordDetailDAO extends MyBatisBaseDao<TSmsRecordDetail, Long> {

    List<TSmsRecordDetail> queryRecordDetailList(Map<String, Object> param);

    Long queryCountRecordDetail(Map<String, Object> param);

}
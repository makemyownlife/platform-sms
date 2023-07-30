package com.courage.platform.sms.admin.service;

import com.courage.platform.sms.admin.domain.TSmsRecordDetail;

import java.util.List;
import java.util.Map;

public interface SmsRecordService {

    List<TSmsRecordDetail> queryRecordDetailList(Map<String, Object> param);

    Long queryCountRecordDetail(Map<String, Object> param);

}

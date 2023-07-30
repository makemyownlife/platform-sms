package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.dao.TSmsRecordDAO;
import com.courage.platform.sms.admin.dao.TSmsRecordDetailDAO;
import com.courage.platform.sms.admin.domain.TSmsRecordDetail;
import com.courage.platform.sms.admin.domain.TSmsTemplate;
import com.courage.platform.sms.admin.service.SmsRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SmsRecordServiceImpl implements SmsRecordService {

    private final Logger logger = LoggerFactory.getLogger(SmsRecordServiceImpl.class);

    @Autowired
    private TSmsRecordDAO recordDAO;

    @Autowired
    private TSmsRecordDetailDAO detailDAO;

    @Override
    public List<TSmsRecordDetail> queryRecordDetailList(Map<String, Object> param) {
        return null;
    }

    @Override
    public Long queryCountRecordDetail(Map<String, Object> param) {
        return null;
    }

}

package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.dao.TSmsRecordDAO;
import com.courage.platform.sms.admin.dao.TSmsRecordDetailDAO;
import com.courage.platform.sms.admin.dao.domain.TSmsRecordDetail;
import com.courage.platform.sms.admin.service.SmsRecordService;
import com.courage.platform.sms.admin.vo.BaseModel;
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
        return detailDAO.queryRecordDetailList(param);
    }

    @Override
    public Long queryCountRecordDetail(Map<String, Object> param) {
        return detailDAO.queryCountRecordDetail(param);
    }

    @Override
    public BaseModel<String> adminSendRecord(String mobile, String templateId) {
        return null;
    }


}

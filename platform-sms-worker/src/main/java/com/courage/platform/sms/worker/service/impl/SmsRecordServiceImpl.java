package com.courage.platform.sms.worker.service.impl;

import com.courage.platform.sms.core.domain.ChannelSendResult;
import com.courage.platform.sms.core.domain.SmsMessage;
import com.courage.platform.sms.dao.SmsRecordDao;
import com.courage.platform.sms.worker.service.SmsRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Transactional
@Service("SmsRecordService")
public class SmsRecordServiceImpl implements SmsRecordService {

    private final static Logger logger = LoggerFactory.getLogger(SmsRecordServiceImpl.class);

    @Autowired
    SmsRecordDao smsRecordDao;

    /*添加短信记录到数据库：*/
    @Override
    public void addSmsRecord(SmsMessage smsMessage, ChannelSendResult sendResult) {
        HashMap map = new HashMap();
        map.put("sms_id", smsMessage.getSmsId());
        map.put("id", 0);
        map.put("mobile", smsMessage.getMobile());
        map.put("content", smsMessage.getContent());
        map.put("channel", smsMessage.getChannel());
        map.put("type", smsMessage.getType());
        map.put("appId", smsMessage.getAppId());
        map.put("ip", smsMessage.getIp());
        map.put("attime", smsMessage.getAttime());
        if (sendResult != null) {
            map.put("send_status", sendResult.getCode());
        }
        //保存短信记录
        smsRecordDao.addSmsRecord(map);
        //保存短信详情
        smsRecordDao.addSmsRecordDetail(sendResult.getMsgIds(), map);
    }

}

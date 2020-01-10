package com.courage.platform.sms.worker.service;

import com.courage.platform.sms.core.domain.ChannelSendResult;
import com.courage.platform.sms.core.domain.SmsMessage;

public interface SmsRecordService {

    /*添加短信记录到数据库：*/
    void addSmsRecord(SmsMessage smsMessage, ChannelSendResult sendStatus);

}

package com.courage.platform.sms.api.service;

import com.courage.platform.sms.dao.SmsAppinfoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsApiService {

    private final static Logger logger = LoggerFactory.getLogger(SmsApiService.class);

    @Autowired
    private SmsAppinfoDao smsAppinfoDao;

}

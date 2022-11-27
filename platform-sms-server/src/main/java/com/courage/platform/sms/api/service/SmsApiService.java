package com.courage.platform.sms.api.service;

import com.courage.platform.sms.dao.SmsAppinfoDao;
import com.courage.platform.sms.dao.domain.SmsAppinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SmsApiService {

    private final static Logger logger = LoggerFactory.getLogger(SmsApiService.class);

    private static ConcurrentHashMap<String, SmsAppinfo> appinfoConcurrentHashMap = new ConcurrentHashMap<String, SmsAppinfo>();

    @Autowired
    private SmsAppinfoDao smsAppinfoDao;

    

}

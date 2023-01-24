package com.courage.platform.sms.worker.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SmsAdapterService {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterService.class);

    private SmsAdapterLoader smsAdapterLoader;

    @PostConstruct
    public void init() {
        this.smsAdapterLoader = new SmsAdapterLoader();
    }

}

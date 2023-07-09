package com.courage.platform.sms.admin.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component(value = "smsAdapterService")
public class SmsAdapterService {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterService.class);

    private volatile boolean running = false;

    @PostConstruct
    public synchronized void init() {

    }

    @PreDestroy
    public synchronized void destroy() {

    }

}

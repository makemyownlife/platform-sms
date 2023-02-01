package com.courage.platform.sms.worker.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SmsAdapterService {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterService.class);

    private volatile boolean running = false;

    private SmsAdapterLoader smsAdapterLoader;

    @PostConstruct
    public synchronized void init() {
        if (running) {
            return;
        }
        try {
            logger.info("start the sms adapters.");
            this.smsAdapterLoader = new SmsAdapterLoader();
            smsAdapterLoader.init();
            running = true;
            logger.info("the sms adapters are running now ......");
        } catch (Exception e) {
            logger.error("something goes wrong when starting up the sms adapters:", e);
        }
    }

}

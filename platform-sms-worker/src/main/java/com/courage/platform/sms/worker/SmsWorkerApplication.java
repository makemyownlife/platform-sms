package com.courage.platform.sms.worker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by zhangyong on 2020/1/4.
 */
@SpringBootApplication(scanBasePackages = "com.courage.platform.sms")
@ServletComponentScan
@EnableScheduling
public class SmsWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsWorkerApplication.class, args);
    }

}

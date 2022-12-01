package com.courage.platform.sms.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by zhangyong on 2020/1/4.
 */
@SpringBootApplication(scanBasePackages = "com.courage.platform.sms")
@ServletComponentScan
@EnableScheduling
@ImportResource(locations = "spring-app.xml")
public class SmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsServerApplication.class, args);
    }

}

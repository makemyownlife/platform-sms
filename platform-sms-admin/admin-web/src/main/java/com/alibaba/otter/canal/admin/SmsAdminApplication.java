package com.alibaba.otter.canal.admin;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SmsAdminApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(SmsAdminApplication.class);
        application.run(args);
    }

}

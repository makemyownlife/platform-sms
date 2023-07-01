package com.courage.platform.sms.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.courage.platform.sms.admin.dao")
public class SmsAdminApplication {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdminApplication.class);

    public static void main(String[] args) {
        logger.info("开始启动短信平台管理服务");
        SpringApplication application = new SpringApplication(SmsAdminApplication.class);
        application.run(args);
        logger.info("结束启动短信平台管理服务");
    }

}

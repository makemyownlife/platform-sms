package com.courage.platform.sms.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoMainApplication {

    private final static Logger logger = LoggerFactory.getLogger(DemoMainApplication.class);

    public static void main(String[] args) {
        logger.info("开始启动短信测试服务");
        SpringApplication application = new SpringApplication(DemoMainApplication.class);
        application.run(args);
        logger.info("结束启动短信短信测试服务");
    }

}

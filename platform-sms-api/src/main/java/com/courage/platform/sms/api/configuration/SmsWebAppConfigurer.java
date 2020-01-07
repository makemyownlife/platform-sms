package com.courage.platform.sms.api.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SmsWebAppConfigurer implements WebMvcConfigurer {

    private final static Logger logger = LoggerFactory.getLogger(SmsWebAppConfigurer.class);

}
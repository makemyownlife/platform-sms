package com.courage.platform.sms.api.configuration;

import com.courage.platform.sms.api.intercepter.ProtocolIntercepter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SmsWebAppConfigurer implements WebMvcConfigurer {

    private final static Logger logger = LoggerFactory.getLogger(SmsWebAppConfigurer.class);

    @Autowired
    private ProtocolIntercepter protocolIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(protocolIntercepter);
    }

}
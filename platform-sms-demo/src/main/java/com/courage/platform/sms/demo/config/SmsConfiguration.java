package com.courage.platform.sms.demo.config;

import com.courage.platform.sms.client.SmsConfig;
import com.courage.platform.sms.client.SmsSenderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangyong on 2023/10/12.
 */
@Configuration
public class SmsConfiguration {

    @Value("${sms.smsServerUrl}")
    private String smsServerUrl;

    @Value("${sms.appKey}")
    private String appKey;

    @Value("${sms.appSecret}")
    private String appSecret;

    @Bean
    public SmsSenderClient createClient() {
        SmsConfig smsConfig = new SmsConfig();
        smsConfig.setAppKey(appKey);
        smsConfig.setSmsServerUrl(smsServerUrl);
        smsConfig.setAppSecret(appSecret);
        SmsSenderClient smsSenderClient = new SmsSenderClient(smsConfig);
        return smsSenderClient;
    }

}

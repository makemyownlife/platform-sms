package com.courage.platform.sms.client;

/**
 * 短信平台 sdk 配置
 */
public class SmsConfig {

    private String smsServerUrl;

    private String appKey;

    private String appSecret;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSmsServerUrl() {
        return smsServerUrl;
    }

    public void setSmsServerUrl(String smsServerUrl) {
        this.smsServerUrl = smsServerUrl;
    }

}

package com.courage.platform.sms.client;

/**
 * 短信平台sdk配置
 */
public class PlatformSmsConfig {

    private String smsServiceUrl;

    private String appKey;

    private String appSecret;

    public String getSmsServiceUrl() {
        return smsServiceUrl;
    }

    public void setSmsServiceUrl(String smsServiceUrl) {
        this.smsServiceUrl = smsServiceUrl;
    }

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

}

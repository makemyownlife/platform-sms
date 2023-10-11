package com.courage.platform.sms.admin.common.utils;

/**
 * 渠道字典枚举
 */
public enum ChannelDictEnum {

    ALIYUN("aliyun", "支付宝", "https://dysmsapi.aliyuncs.com"),
    EMAY("emay", "艺美", ""),
    TENCENT("tencent", "腾讯云", "https://sms.tencentcloudapi.com");

    private final String channelType;

    private final String channelName;

    private final String domain;

    ChannelDictEnum(String channelType, String channelName, String domain) {
        this.channelType = channelType;
        this.channelName = channelName;
        this.domain = domain;
    }

    public String getChannelType() {
        return channelType;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getDomain() {
        return domain;
    }

}

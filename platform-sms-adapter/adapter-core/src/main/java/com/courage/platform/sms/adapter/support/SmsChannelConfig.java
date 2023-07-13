package com.courage.platform.sms.adapter.support;

/**
 * 短信通道配置
 */
public class SmsChannelConfig {

    //通道账号编号
    private Long id;

    /**
     * 通道类型: aliyun/emay
     */
    private String channelType;

    //通道appkey
    private String channelAppkey;

    //通道appsecet
    private String channelAppsecret;

    //访问地址
    private String channelDomain;

    //属性 json 格式
    private String extProperties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelDomain() {
        return channelDomain;
    }

    public void setChannelDomain(String channelDomain) {
        this.channelDomain = channelDomain;
    }

    public String getExtProperties() {
        return extProperties;
    }

    public void setExtProperties(String extProperties) {
        this.extProperties = extProperties;
    }

    public String getChannelAppkey() {
        return channelAppkey;
    }

    public void setChannelAppkey(String channelAppkey) {
        this.channelAppkey = channelAppkey;
    }

    public String getChannelAppsecret() {
        return channelAppsecret;
    }

    public void setChannelAppsecret(String channelAppsecret) {
        this.channelAppsecret = channelAppsecret;
    }
}

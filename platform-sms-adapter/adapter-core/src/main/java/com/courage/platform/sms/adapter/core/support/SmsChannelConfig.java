package com.courage.platform.sms.adapter.core.support;

import java.util.Date;

/**
 * 短信通道配置
 */
public class SmsChannelConfig {

    //通道账号编号
    private Long id;

    //通道类型: aliyun/emay
    private String channelType;

    //渠道名称： 阿里云/亿美 (冗余使用)
    private String channelName;

    //通道appkey
    private String channelAppKey;

    //通道appsecet
    private String channelAppSecret;

    //访问地址
    private String channelDomain;

    //属性 json 格式
    private String extProperties;

    //状态 0：正常 1：失效
    private Integer status;

    //创建时间
    private Date creatTime;

    //修改时间
    private Date updateTime;

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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

    public String getChannelAppKey() {
        return channelAppKey;
    }

    public void setChannelAppKey(String channelAppKey) {
        this.channelAppKey = channelAppKey;
    }

    public String getChannelAppSecret() {
        return channelAppSecret;
    }

    public void setChannelAppSecret(String channelAppSecret) {
        this.channelAppSecret = channelAppSecret;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}

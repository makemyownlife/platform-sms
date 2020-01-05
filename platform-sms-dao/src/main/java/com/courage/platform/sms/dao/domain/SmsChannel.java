package com.courage.platform.sms.dao.domain;

import java.util.Date;

public class SmsChannel {

    private int id;

    private int channelId;

    private String channelName;

    private int channelType;

    private String channelImpl;

    private String channelUser;

    private String channelPassword;

    private String channelUrl;

    private String extProperty;

    private int status;

    private int sendOrder = 1;

    private Date createTime;

    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public String getChannelImpl() {
        return channelImpl;
    }

    public void setChannelImpl(String channelImpl) {
        this.channelImpl = channelImpl;
    }

    public String getChannelUser() {
        return channelUser;
    }

    public void setChannelUser(String channelUser) {
        this.channelUser = channelUser;
    }

    public String getChannelPassword() {
        return channelPassword;
    }

    public void setChannelPassword(String channelPassword) {
        this.channelPassword = channelPassword;
    }

    public String getChannelUrl() {
        return channelUrl;
    }

    public void setChannelUrl(String channelUrl) {
        this.channelUrl = channelUrl;
    }

    public String getExtProperty() {
        return extProperty;
    }

    public void setExtProperty(String extProperty) {
        this.extProperty = extProperty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSendOrder() {
        return sendOrder;
    }

    public void setSendOrder(int sendOrder) {
        this.sendOrder = sendOrder;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}

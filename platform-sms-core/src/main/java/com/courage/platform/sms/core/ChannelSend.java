package com.courage.platform.sms.core;

import com.courage.platform.sms.core.domain.ChannelSendResult;
import com.courage.platform.sms.core.domain.SmsMessage;
import com.courage.platform.sms.core.domain.SmsTypeEnum;

public abstract class ChannelSend implements Comparable<ChannelSend>{

    private String channelName;

    private String channelId;

    private String channelUser;

    private String channelPassword;

    private String channelUrl;

    private String extProperty;

    private int sendOrder;

    private SmsTypeEnum smsTypeEnum;

    public ChannelSend() {
    }

    public void setProperty(String channelName, String channelId, String channelUser, String channelPassword, String channelUrl, String extProperty, int sendOrder
    ) {
        this.channelName = channelName;
        this.channelId = channelId;
        this.channelUser = channelUser;
        this.channelPassword = channelPassword;
        this.channelUrl = channelUrl;
        this.extProperty = extProperty;
        this.sendOrder = sendOrder;
    }

    public abstract ChannelSendResult send(SmsMessage smsMessage);


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public int getSendOrder() {
        return sendOrder;
    }

    public void setSendOrder(int sendOrder) {
        this.sendOrder = sendOrder;
    }

    public SmsTypeEnum getSmsTypeEnum() {
        return smsTypeEnum;
    }

    public void setSmsTypeEnum(SmsTypeEnum smsTypeEnum) {
        this.smsTypeEnum = smsTypeEnum;
    }

    public int compareTo(ChannelSend o) {
        if(this.sendOrder > o.getSendOrder()){
            return 1;
        }else if (this.sendOrder < o.getSendOrder()){
            return -1;
        }else {
            return  Math.random()>0.5?1:-1;
        }
    }
}

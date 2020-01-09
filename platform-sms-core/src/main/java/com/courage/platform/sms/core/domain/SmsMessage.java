package com.courage.platform.sms.core.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class SmsMessage implements Serializable {

    private static final long serialVersionUID = -7423275617110862103L;

    private String smsId;

    //发送方ip
    private String ip;

    //短信内容
    private String content;

    //普通 or 营销
    private int type;

    //手机号
    private String mobile;

    //调用方唯一id
    private String appId;

    //渠道
    private String channel;

    //TemplateId
    private Integer templateId;

    //Tags填充模板的内容
    private ArrayList<String> tags;

    //默认为空
    private String attime = "";

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getAttime() {
        return attime;
    }

    public void setAttime(String attime) {
        this.attime = attime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SmsMessage{" +
                "smsId='" + smsId + '\'' +
                ", ip='" + ip + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", mobile='" + mobile + '\'' +
                ", appId='" + appId + '\'' +
                ", channel='" + channel + '\'' +
                ", templateId=" + templateId +
                ", tags='" + tags + '\'' +
                ", attime='" + attime + '\'' +
                '}';
    }
}

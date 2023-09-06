package com.courage.platform.sms.admin.domain;

import java.io.Serializable;
import java.util.Date;

public class TSmsRecordDetail implements Serializable {

    /**
     * 自增Id
     */
    private Long id;

    /**
     * t_sms_record表外键
     */
    private Long recordId;

    /**
     * 发送方appId 
     */
    private Integer appId;

    /**
     * 短信内容
     */
    private String content;

    /**
     * -1：待发送 /  0：已发送  / 1 : 发送失败
     */
    private Integer sendStatus;

    /**
     * 短信报告 0 ： 待回执  1：发送成功 2 : 发送失败  
     */
    private Integer reportStatus;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 三方短信ID
     */
    private String msgid;

    /**
     * 渠道
     */
    private Integer channelId;

    /**
     * 消息发送时间
     */
    private Date senderTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
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

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public Date getSenderTime() {
        return senderTime;
    }

    public void setSenderTime(Date senderTime) {
        this.senderTime = senderTime;
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

    public Integer getSendStatus() {
        return sendStatus;
    }

    public Integer getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Integer reportStatus) {
        this.reportStatus = reportStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
}
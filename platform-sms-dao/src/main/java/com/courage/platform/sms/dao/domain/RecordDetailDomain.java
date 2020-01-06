package com.courage.platform.sms.dao.domain;

import java.util.Date;

public class RecordDetailDomain {
    private long id;
    private long record_id;//t_sms_record表外键
    private String content; //短信内容
    private int report_status;//短信报告状态
    private String report_time; //短信状态报告时间
    private String mobile;//手机号码
    private String msgid;//短信ID
    private String channel;//渠道
    private String app_id;//发送方appId
    private Date sender_time;//消息发送时间
    private String receive_time;//短信接收时间
    private String create_time;//创建时间
    private String update_time;//更新时间
    private int send_status;//-1：待发送 / 1：已发送 /0 : 发送失败
    private String app_name;//调用方系统名称

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    @Override
    public String toString() {
        return "RecordDetailDomain{" + "id=" + id + ", record_id=" + record_id + ", content='" + content + '\'' + ", report_status=" + report_status + ", report_time='" + report_time + '\'' + ", mobile='" + mobile + '\'' + ", msgid='" + msgid + '\'' + ", channel='" + channel + '\'' + ", app_id='" + app_id + '\'' + ", sender_time='" + sender_time + '\'' + ", receive_time='" + receive_time + '\'' + ", create_time='" + create_time + '\'' + ", update_time='" + update_time + '\'' + ", send_status=" + send_status + '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecord_id() {
        return record_id;
    }

    public void setRecord_id(long record_id) {
        this.record_id = record_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getReport_status() {
        return report_status;
    }

    public void setReport_status(int report_status) {
        this.report_status = report_status;
    }

    public String getReport_time() {
        return report_time;
    }

    public void setReport_time(String report_time) {
        this.report_time = report_time;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public Date getSender_time() {
        return sender_time;
    }

    public void setSender_time(Date sender_time) {
        this.sender_time = sender_time;
    }

    public String getReceive_time() {
        return receive_time;
    }

    public void setReceive_time(String receive_time) {
        this.receive_time = receive_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getSend_status() {
        return send_status;
    }

    public void setSend_status(int send_status) {
        this.send_status = send_status;
    }

}

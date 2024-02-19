package cn.javayong.platform.sms.admin.domain;

import java.io.Serializable;
import java.util.Date;

public class TSmsRecord implements Serializable {
    /**
     * 主键 雪花算法
     */
    private Long id;

    /**
     * 区位码
     */
    private String nationcode;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 调用服务方
     */
    private Long appId;

    /**
     * 0：普通短信 / 1 ：营销短信 （群发）
     */
    private Integer recordType;
    /**
     * 模版编号
     */
    private Long templateId;

    /**
     * 模版参数
     */
    private String templateParam;

    /**
     * 指定发送时间
     */
    private String attime;

    /**
     * -1：待发送 / 0：已发送  /1  : 发送失败
     */
    private Integer sendStatus;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNationcode() {
        return nationcode;
    }

    public void setNationcode(String nationcode) {
        this.nationcode = nationcode;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getAttime() {
        return attime;
    }

    public void setAttime(String attime) {
        this.attime = attime;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
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

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

}
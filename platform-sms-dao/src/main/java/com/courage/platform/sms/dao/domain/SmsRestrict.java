package com.courage.platform.sms.dao.domain;

import java.util.Date;

/**
 * 短信限制实体类<br>
 */
public class SmsRestrict {

    private int id;

    private String restrictKey;

    private int restrictType;

    private int restrictNum;

    private int status;

    private Date createTime;

    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRestrictKey() {
        return restrictKey;
    }

    public void setRestrictKey(String restrictKey) {
        this.restrictKey = restrictKey;
    }

    public int getRestrictType() {
        return restrictType;
    }

    public void setRestrictType(int restrictType) {
        this.restrictType = restrictType;
    }

    public int getRestrictNum() {
        return restrictNum;
    }

    public void setRestrictNum(int restrictNum) {
        this.restrictNum = restrictNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

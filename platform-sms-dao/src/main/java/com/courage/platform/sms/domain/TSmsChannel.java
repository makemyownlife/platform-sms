package com.courage.platform.sms.domain;

import java.io.Serializable;
import java.util.Date;

public class TSmsChannel implements Serializable {
    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 渠道Id
     */
    private Byte channelId;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道类型 0：行业 1：营销
     */
    private Byte channelType;

    /**
     * 渠道实现类
     */
    private String channelImpl;

    /**
     * 渠道用户名
     */
    private String channelUser;

    /**
     * 渠道密码
     */
    private String channelPassword;

    /**
     * 渠道请求地址
     */
    private String channelUrl;

    /**
     * 备用参数
     */
    private String extProperty;

    /**
     * 状态0：启用 1：禁用
     */
    private Byte status;

    private Date createTime;

    private Date updateTime;

    private Integer sendOrder;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getChannelId() {
        return channelId;
    }

    public void setChannelId(Byte channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Byte getChannelType() {
        return channelType;
    }

    public void setChannelType(Byte channelType) {
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
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

    public Integer getSendOrder() {
        return sendOrder;
    }

    public void setSendOrder(Integer sendOrder) {
        this.sendOrder = sendOrder;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TSmsChannel other = (TSmsChannel) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getChannelId() == null ? other.getChannelId() == null : this.getChannelId().equals(other.getChannelId()))
            && (this.getChannelName() == null ? other.getChannelName() == null : this.getChannelName().equals(other.getChannelName()))
            && (this.getChannelType() == null ? other.getChannelType() == null : this.getChannelType().equals(other.getChannelType()))
            && (this.getChannelImpl() == null ? other.getChannelImpl() == null : this.getChannelImpl().equals(other.getChannelImpl()))
            && (this.getChannelUser() == null ? other.getChannelUser() == null : this.getChannelUser().equals(other.getChannelUser()))
            && (this.getChannelPassword() == null ? other.getChannelPassword() == null : this.getChannelPassword().equals(other.getChannelPassword()))
            && (this.getChannelUrl() == null ? other.getChannelUrl() == null : this.getChannelUrl().equals(other.getChannelUrl()))
            && (this.getExtProperty() == null ? other.getExtProperty() == null : this.getExtProperty().equals(other.getExtProperty()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSendOrder() == null ? other.getSendOrder() == null : this.getSendOrder().equals(other.getSendOrder()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getChannelId() == null) ? 0 : getChannelId().hashCode());
        result = prime * result + ((getChannelName() == null) ? 0 : getChannelName().hashCode());
        result = prime * result + ((getChannelType() == null) ? 0 : getChannelType().hashCode());
        result = prime * result + ((getChannelImpl() == null) ? 0 : getChannelImpl().hashCode());
        result = prime * result + ((getChannelUser() == null) ? 0 : getChannelUser().hashCode());
        result = prime * result + ((getChannelPassword() == null) ? 0 : getChannelPassword().hashCode());
        result = prime * result + ((getChannelUrl() == null) ? 0 : getChannelUrl().hashCode());
        result = prime * result + ((getExtProperty() == null) ? 0 : getExtProperty().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSendOrder() == null) ? 0 : getSendOrder().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", channelId=").append(channelId);
        sb.append(", channelName=").append(channelName);
        sb.append(", channelType=").append(channelType);
        sb.append(", channelImpl=").append(channelImpl);
        sb.append(", channelUser=").append(channelUser);
        sb.append(", channelPassword=").append(channelPassword);
        sb.append(", channelUrl=").append(channelUrl);
        sb.append(", extProperty=").append(extProperty);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", sendOrder=").append(sendOrder);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
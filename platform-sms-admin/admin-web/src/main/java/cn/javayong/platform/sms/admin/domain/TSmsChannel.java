package cn.javayong.platform.sms.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

public class TSmsChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键自增
     */
    private Integer id;

    /**
     * 渠道名称
     */
    private String channelType;

    /**
     * 渠道名称
     */
    private String channelName;

    /**
     * 渠道用户名
     */
    private String channelAppkey;

    /**
     * 渠道密码
     */
    @JsonIgnore
    private String channelAppsecret;

    /**
     * 渠道请求地址
     */
    private String channelDomain;

    /**
     * 备用参数
     */
    private String extProperties;

    /**
     *  0：启用 1：禁用
     */
    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer sendOrder;

    private String md5Value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
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

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

}
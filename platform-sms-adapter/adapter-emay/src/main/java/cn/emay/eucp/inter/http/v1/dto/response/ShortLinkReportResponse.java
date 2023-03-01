package cn.emay.eucp.inter.http.v1.dto.response;

import java.io.Serializable;
import java.util.Date;

public class ShortLinkReportResponse implements Serializable {

	/**
	*/
	private static final long serialVersionUID = 1L;

	private String mobile;// 手机号
	private Date accessTime;// 访问时间
	private String equipment;// 设备
	private String browser;// 浏览器
	private String longLink;// 长链接
	private String accessAddress;// 访问地址
	private String userAgent;// user-agent
	private String appId;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getAccessTime() {
		return accessTime;
	}

	public void setAccessTime(Date accessTime) {
		this.accessTime = accessTime;
	}

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getLongLink() {
		return longLink;
	}

	public void setLongLink(String longLink) {
		this.longLink = longLink;
	}

	public String getAccessAddress() {
		return accessAddress;
	}

	public void setAccessAddress(String accessAddress) {
		this.accessAddress = accessAddress;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

}

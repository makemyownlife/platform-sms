package cn.emay.sdk.core.dto.sms.common;

import java.io.Serializable;

public class MobileAndContent implements Serializable {

	private static final long serialVersionUID = 1L;

	private String mobile;

	private String content;

	public MobileAndContent() {

	}

	public MobileAndContent(String mobile, String content) {
		this.mobile = mobile;
		this.content = content;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

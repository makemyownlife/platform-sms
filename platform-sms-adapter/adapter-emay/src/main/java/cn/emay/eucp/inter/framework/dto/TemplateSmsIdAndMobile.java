package cn.emay.eucp.inter.framework.dto;

import java.io.Serializable;
import java.util.Map;

public class TemplateSmsIdAndMobile implements Serializable {

	/**
	*/
	private static final long serialVersionUID = 1L;
	private String mobile;
	private String customSmsId;
	private Map<String, String> content;

	public TemplateSmsIdAndMobile() {

	}

	public TemplateSmsIdAndMobile(String mobile, String customSmsId) {
		this.mobile = mobile;
		this.customSmsId = customSmsId;
	}

	public TemplateSmsIdAndMobile(String mobile, String customSmsId, Map<String, String> content) {
		this.mobile = mobile;
		this.customSmsId = customSmsId;
		this.content = content;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Map<String, String> getContent() {
		return content;
	}

	public void setContent(Map<String, String> content) {
		this.content = content;
	}

	public String getCustomSmsId() {
		return customSmsId;
	}

	public void setCustomSmsId(String customSmsId) {
		this.customSmsId = customSmsId;
	}


}

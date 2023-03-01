package cn.emay.eucp.inter.framework.dto;

import java.io.Serializable;

/**
 * 自定义SMSID 手机号 内容
 * @author Frank
 *
 */
public class CustomSmsIdAndMobileAndContent implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String customSmsId;
	
	private String mobile;
	
	private String content;

	public CustomSmsIdAndMobileAndContent(){
		
	}
	
	public CustomSmsIdAndMobileAndContent(String customSmsId,String mobile,String content){
		this.customSmsId = customSmsId;
		this.mobile = mobile;
		this.content = content;
	}
	
	public String getCustomSmsId() {
		return customSmsId;
	}

	public void setCustomSmsId(String customSmsId) {
		this.customSmsId = customSmsId;
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

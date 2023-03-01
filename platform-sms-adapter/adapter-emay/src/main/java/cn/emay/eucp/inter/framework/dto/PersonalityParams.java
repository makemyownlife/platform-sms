package cn.emay.eucp.inter.framework.dto;

import java.io.Serializable;

/**
 * 自定义SMSID 手机号 内容
 * @author Frank
 *
 */
public class PersonalityParams implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String customSmsId;
	
	private String mobile;
	
	private String content;
	
	private String extendedCode;
	
	private String timerTime;

	public PersonalityParams(){
		
	}
	
	public PersonalityParams(String customSmsId,String mobile,String content,String extendedCode,String timerTime){
		this.customSmsId = customSmsId;
		this.mobile = mobile;
		this.content = content;
		this.timerTime = timerTime;
		this.extendedCode = extendedCode;
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

	public String getExtendedCode() {
		return extendedCode;
	}

	public void setExtendedCode(String extendedCode) {
		this.extendedCode = extendedCode;
	}

	public String getTimerTime() {
		return timerTime;
	}

	public void setTimerTime(String timerTime) {
		this.timerTime = timerTime;
	}

}

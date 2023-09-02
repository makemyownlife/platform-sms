package cn.emay.sdk.core.dto.sms.common;

import java.io.Serializable;

/**
 * 自定义SMSID与手机号
 * @author Frank
 *
 */
public class SmsIdAndMobile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String smsId;
	
	private String customSmsId;
	
	private String mobile;
	
	public SmsIdAndMobile(){
		
	}
	
	public SmsIdAndMobile(String smsId,String customSmsId,String mobile){
		this.smsId = smsId;
		this.customSmsId = customSmsId;
		this.mobile = mobile;
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

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

}

package cn.emay.eucp.inter.http.v1.dto.response;

import java.io.Serializable;

/**
 * 单条短信发送响应
 * @author Frank
 *
 */
public class SmsResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统唯一smsId
	 */
	private String smsId;
	
	private String mobile;
	
	private String customSmsId;
	
	public SmsResponse(){
		
	}
	
	public SmsResponse(String smsId,String mobile,String customSmsId){
		this.smsId = smsId;
		this.mobile = mobile;
		this.customSmsId = customSmsId;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCustomSmsId() {
		return customSmsId;
	}

	public void setCustomSmsId(String customSmsId) {
		this.customSmsId = customSmsId;
	}

}

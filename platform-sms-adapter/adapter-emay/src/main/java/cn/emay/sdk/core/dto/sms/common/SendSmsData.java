package cn.emay.sdk.core.dto.sms.common;

import java.io.Serializable;

/**
 * 发送短信实体类
 * 
 * @author Hcs
 *
 */
public class SendSmsData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String smsId;//

	private String customerId;

	private String smscontent;// 短信内容

	private String sendTime;// 定时时间

	private String mobile; // smsIdAndMobiles

	private String extendedCode;// 扩展码

	public String getSmscontent() {
		return smscontent;
	}

	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getExtendedCode() {
		return extendedCode;
	}

	public void setExtendedCode(String extendedCode) {
		this.extendedCode = extendedCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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

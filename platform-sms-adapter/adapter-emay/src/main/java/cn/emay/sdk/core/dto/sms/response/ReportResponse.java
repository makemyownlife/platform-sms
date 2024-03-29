package cn.emay.sdk.core.dto.sms.response;

import java.io.Serializable;

/**
 * 状态报告数据
 * 
 * @author Frank
 *
 */
public class ReportResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String interSmsId;// 接口自定义ID

	private String smsId;// 短信唯一标识

	private String customSmsId;// 客户自定义SmsId

	private String state;// 成功失败标识

	private String desc;// 状态报告描述

	private String mobile;// 手机号

	private String receiveTime;// 状态报告返回时间

	private String submitTime;// 信息提交时间

	private String extendedCode;// 扩展码

	public ReportResponse(String smsId, String customSmsId, String state, String desc, String mobile, String receiveTime, String submitTime, String extendedCode) {
		this.smsId = smsId;
		this.customSmsId = customSmsId;
		this.state = state;
		this.desc = desc;
		this.mobile = mobile;
		this.receiveTime = receiveTime;
		this.submitTime = submitTime;
		this.extendedCode = extendedCode;
	}

	public ReportResponse() {
	}

	public String getCustomSmsId() {
		return customSmsId;
	}

	public void setCustomSmsId(String customSmsId) {
		this.customSmsId = customSmsId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getExtendedCode() {
		return extendedCode;
	}

	public void setExtendedCode(String extendedCode) {
		this.extendedCode = extendedCode;
	}

	public String getInterSmsId() {
		return interSmsId;
	}

	public void setInterSmsId(String interSmsId) {
		this.interSmsId = interSmsId;
	}

}

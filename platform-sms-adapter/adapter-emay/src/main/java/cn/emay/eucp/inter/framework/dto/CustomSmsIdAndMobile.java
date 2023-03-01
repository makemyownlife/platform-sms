package cn.emay.eucp.inter.framework.dto;

import java.io.Serializable;

/**
 * 自定义SMSID 手机号
 * @author Frank
 *
 */
public class CustomSmsIdAndMobile implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String customSmsId;
	
	private String mobile;
	
	public CustomSmsIdAndMobile(){
		
	}
	
	public CustomSmsIdAndMobile(String customSmsId,String mobile){
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

}

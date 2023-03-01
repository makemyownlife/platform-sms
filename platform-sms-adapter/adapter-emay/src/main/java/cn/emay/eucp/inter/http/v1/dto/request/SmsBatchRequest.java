package cn.emay.eucp.inter.http.v1.dto.request;

import cn.emay.eucp.inter.framework.dto.CustomSmsIdAndMobile;


/**
 * 批量短信发送参数
 * @author Frank
 *
 */
public class SmsBatchRequest extends SmsBaseRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 手机号与自定义SmsId
	 */
	private CustomSmsIdAndMobile[] smses;
	
	/**
	 * 短信内容
	 */
	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CustomSmsIdAndMobile[] getSmses() {
		return smses;
	}

	public void setSmses(CustomSmsIdAndMobile[] smses) {
		this.smses = smses;
	}

}

package cn.emay.eucp.inter.http.v1.dto.request;

import cn.emay.eucp.inter.framework.dto.CustomSmsIdAndMobile;


/**
 * 短链短信发送参数
 * 
 * @author Frank
 *
 */
public class SmsShortLinkBatchRequest extends SmsBaseRequest {

	private static final long serialVersionUID = 1L;

	private String url;

	private String shortLinkRule;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getShortLinkRule() {
		return shortLinkRule;
	}

	public void setShortLinkRule(String shortLinkRule) {
		this.shortLinkRule = shortLinkRule;
	}

}

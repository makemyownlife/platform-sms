package cn.emay.eucp.inter.http.v1.dto.request;

import cn.emay.eucp.inter.framework.dto.CustomSmsIdAndMobileAndContent;


/**
 * 批量短信发送参数
 * @author Frank
 *
 */
public class SmsPersonalityRequest extends SmsBaseRequest {

	private static final long serialVersionUID = 1L;

	private CustomSmsIdAndMobileAndContent[] smses;

	public CustomSmsIdAndMobileAndContent[] getSmses() {
		return smses;
	}

	public void setSmses(CustomSmsIdAndMobileAndContent[] smses) {
		this.smses = smses;
	}
	

}

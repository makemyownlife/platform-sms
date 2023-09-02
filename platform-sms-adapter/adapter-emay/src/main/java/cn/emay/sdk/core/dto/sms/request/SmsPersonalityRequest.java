package cn.emay.sdk.core.dto.sms.request;

import cn.emay.sdk.core.dto.sms.common.CustomSmsIdAndMobileAndContent;

/**
 * 批量短信发送参数
 * 
 * @author Frank
 *
 */
public class SmsPersonalityRequest extends SmsBaseRequest {

	private static final long serialVersionUID = 1L;

	private CustomSmsIdAndMobileAndContent[] smses;

	public SmsPersonalityRequest() {
		super();
	}

	public SmsPersonalityRequest(CustomSmsIdAndMobileAndContent[] smses, String timerTime, String extendedCode) {
		super(timerTime, extendedCode);
		this.smses = smses;
	}

	public CustomSmsIdAndMobileAndContent[] getSmses() {
		return smses;
	}

	public void setSmses(CustomSmsIdAndMobileAndContent[] smses) {
		this.smses = smses;
	}

}

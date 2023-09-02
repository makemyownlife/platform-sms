package cn.emay.sdk.core.dto.sms.request;

import cn.emay.sdk.core.dto.sms.common.PersonalityParams;

/**
 * 批量短信发送参数
 * 
 * @author Frank
 *
 */
public class SmsPersonalityAllRequest extends SmsBaseRequest {

	private static final long serialVersionUID = 1L;

	private PersonalityParams[] smses;

	public SmsPersonalityAllRequest() {
		super();
	}

	public SmsPersonalityAllRequest(PersonalityParams[] smses) {
		this.smses = smses;
	}

	public PersonalityParams[] getSmses() {
		return smses;
	}

	public void setSmses(PersonalityParams[] smses) {
		this.smses = smses;
	}

}

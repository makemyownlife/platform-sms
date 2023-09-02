package cn.emay.sdk.core.dto.sms.request;

import cn.emay.sdk.core.dto.sms.common.MobileAndContent;

public class SmsPersonalitySimpleRequest extends SmsBaseRequest {

	private static final long serialVersionUID = 1L;

	private String customSmsId;

	private MobileAndContent[] mobileAndContents;

	public String getCustomSmsId() {
		return customSmsId;
	}

	public void setCustomSmsId(String customSmsId) {
		this.customSmsId = customSmsId;
	}

	public MobileAndContent[] getMobileAndContents() {
		return mobileAndContents;
	}

	public void setMobileAndContents(MobileAndContent[] mobileAndContents) {
		this.mobileAndContents = mobileAndContents;
	}

}

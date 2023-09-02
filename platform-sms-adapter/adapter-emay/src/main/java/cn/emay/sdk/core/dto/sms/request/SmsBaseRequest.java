package cn.emay.sdk.core.dto.sms.request;

/**
 * 基础请求参数
 * 
 * @author Hcs
 *
 */
public class SmsBaseRequest extends BaseRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 定时时间 yyyy-MM-dd HH:mm:ss
	 */
	private String timerTime;

	/**
	 * 扩展码
	 */
	private String extendedCode;

	public SmsBaseRequest() {
		super();
	}

	public SmsBaseRequest(String timerTime, String extendedCode) {
		super();
		this.timerTime = timerTime;
		this.extendedCode = extendedCode;
	}

	public String getTimerTime() {
		return timerTime;
	}

	public void setTimerTime(String timerTime) {
		this.timerTime = timerTime;
	}

	public String getExtendedCode() {
		return extendedCode;
	}

	public void setExtendedCode(String extendedCode) {
		this.extendedCode = extendedCode;
	}

}

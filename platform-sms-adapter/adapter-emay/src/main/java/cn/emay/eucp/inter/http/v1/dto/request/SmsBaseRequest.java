package cn.emay.eucp.inter.http.v1.dto.request;


public class SmsBaseRequest extends BaseRequest {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 定时时间
	 * yyyy-MM-dd HH:mm:ss
	 */
	private String timerTime;
	
	/**
	 * 扩展码
	 */
	private String extendedCode;

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

package cn.emay.sdk.core.dto.sms.request;

/**
 * 状态报告重新获取参数
 * 
 * @author Hcs
 *
 */
public class RetrieveReportRequest extends BaseRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 状态报告开始时间(必填，对应短信提交时间，只能为当前时间30天前范围，不能大于当前时间) 格式：yyyyMMddHHmmss 14位
	 */
	private String startTime;
	/**
	 * 状态报告结束时间(必填,对应短信提交时间,只能为当前时间30天前范围,不能大于当前时间,应大于开始时间,开始结束时间最大间隔为10分钟,建议5分钟以内)
	 * 格式：yyyyMMddHHmmss 14位
	 */
	private String endTime;

	/**
	 * 短信的smsId(选填)，多个用半角逗号分隔，最多1000个，如 123123123,321321321
	 */
	private String smsId;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}

}

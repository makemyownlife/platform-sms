package cn.emay.sdk.core.dto.sms.response;

import java.io.Serializable;

/**
 * 状态报告重新获取结果
 * 
 * @author Hcs
 *
 */
public class RetrieveReportResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 响应状态码
	 */
	private String code;

	public RetrieveReportResponse() {

	}

	public RetrieveReportResponse(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

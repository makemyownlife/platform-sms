package cn.emay.sdk.core.dto.sms.common;

public class ResultModel<T> {

	private String code;
	private T result;

	public ResultModel(String code, T result) {
		this.code = code;
		this.result = result;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}

package cn.emay.sdk.util.exception;

public class SDKParamsException extends Exception {

	private static final long serialVersionUID = 1L;

	public SDKParamsException() {
		super();
	}

	public SDKParamsException(String message) {
		super(message);
	}

	public SDKParamsException(Throwable throwable) {
		super(throwable);
	}

	public SDKParamsException(String message, Throwable throwable) {
		super(message, throwable);
	}

}

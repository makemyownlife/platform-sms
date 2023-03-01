package cn.emay.util.http;

/**
 * HTTP 访问结果编码
 * 
 * @author Frank
 *
 */
public enum HttpResultCode {

	SUCCESS("成功", "SUCCESS"), //
	ERROR_URL_NULL("URL为空", "ERROR-URL-NULL"), //
	ERROR_URL("URL访问失败", "ERROR-URL"), //
	ERROR_HTTPS_SSL("HTTPS异常", "ERROR-HTTPS-SSL"), //
	ERROR_METHOD("HTTP方法无法识别", "ERROR-METHOD"), //
	ERROR_CHARSET("编码错误", "ERROR-CHARSET"), //
	ERROR_CONNECT("访问失败", "ERROR-CONNECT"), //
	ERROR_TIMEOUT("访问超时", "ERROR-TIMEOUT"), //

	;

	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;

	private HttpResultCode(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public static String findNameByCode(String code) {
		for (HttpResultCode oc : HttpResultCode.values()) {
			if (oc.getCode().equals(code)) {
				return oc.getName();
			}
		}
		return null;
	}

	public static String findCodeByName(String name) {
		for (HttpResultCode oc : HttpResultCode.values()) {
			if (oc.getName().equals(name)) {
				return oc.getCode();
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}

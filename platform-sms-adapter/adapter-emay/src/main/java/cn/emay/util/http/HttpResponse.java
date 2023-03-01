package cn.emay.util.http;

import java.util.List;
import java.util.Map;

/**
 * Http响应
 * 
 * @author Frank
 *
 */
public class HttpResponse<T> {

	/**
	 * Http 结果代码
	 */
	private HttpResultCode resultCode;

	/**
	 * Http链接Code
	 */
	private int httpCode;

	/**
	 * Http响应头
	 */
	private Map<String, String> headers;

	/**
	 * http响应Cookies
	 */
	private List<String> cookies;
	/**
	 * http字符集
	 */
	private String charSet;
	/**
	 * http响应数据
	 */
	private T result;

	/**
	 * 
	 * @param resultCode
	 *            Http 结果代码
	 * @param httpCode
	 *            Http链接Code
	 * @param headers
	 *            Http响应头
	 * @param cookies
	 *            http响应Cookies
	 * @param charSet
	 *            http字符集
	 * @param result
	 *            http响应数据
	 */
	public HttpResponse(HttpResultCode resultCode, int httpCode, Map<String, String> headers, List<String> cookies, String charSet, T result) {
		this.resultCode = resultCode;
		this.httpCode = httpCode;
		this.headers = headers;
		this.cookies = cookies;
		this.charSet = charSet;
		this.result = result;
	}

	public HttpResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(HttpResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public int getHttpCode() {
		return httpCode;
	}

	public void setHttpCode(int httpCode) {
		this.httpCode = httpCode;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public List<String> getCookies() {
		return cookies;
	}

	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

}

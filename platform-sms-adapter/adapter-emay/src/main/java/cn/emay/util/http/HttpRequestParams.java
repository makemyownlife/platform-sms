package cn.emay.util.http;

import java.util.Map;

/**
 * Http参数
 * 
 * @author Frank
 *
 * @param <T>
 *            传输数据类型
 */
public class HttpRequestParams<T> {

	private String url;// URL
	private String charSet = "UTF-8";// 编码
	private String method = "GET";// Http方法
	private Map<String, String> headers;// 头信息
	private String cookies;// cookie信息
	private T params;// 传输数据

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getCookies() {
		return cookies;
	}

	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	public T getParams() {
		return params;
	}

	public void setParams(T params) {
		this.params = params;
	}

}

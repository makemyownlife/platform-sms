package cn.emay.sdk.util.http.request;

import java.util.Map;

/**
 * Http请求实体
 * 
 * @author Frank
 *
 * @param <T>
 *            传输数据类型
 */
public abstract class EmayHttpRequest<T extends Object> {

	private String url;// URL
	private String charSet = "UTF-8";// 编码
	private String method = "GET";// Http方法
	private Map<String, String> headers;// 头信息
	private String cookies;// cookie信息
	private T params;// 传输数据

	/**
	 * 
	 * @param url
	 *            URL
	 * @param charSet
	 *            编码
	 * @param method
	 *            Http方法
	 * @param headers
	 *            头信息
	 * @param cookies
	 *            cookie信息
	 * @param params
	 *            传输数据
	 */
	public EmayHttpRequest(String url, String charSet, String method, Map<String, String> headers, String cookies, T params) {
		if (url != null) {
			this.url = url.trim();
		}
		if (charSet != null) {
			this.charSet = charSet;
		}
		if (method != null) {
			this.method = method;
		}
		this.headers = headers;
		this.cookies = cookies;
		this.params = params;
		this.fillGetUrl();
	}

	/**
	 * 将传输数据转换为byte[]类型
	 * 
	 * @return
	 */
	public abstract byte[] paramsToBytesForPost();

	/**
	 * 将传输数据转换为String类型
	 * 
	 * @return
	 */
	public abstract String paramsToStringForGet();

	/**
	 * 是否https请求
	 * 
	 * @return
	 */
	public boolean isHttps() {
		if (url == null) {
			return false;
		}
		if (url.startsWith("https")) {
			return true;
		}
		return false;
	}

	/**
	 * 构建GET URL
	 */
	private void fillGetUrl() {
		if (url == null || params == null) {
			return;
		}
		if (this.getMethod().equalsIgnoreCase("GET")) {
			String getprams = this.paramsToStringForGet();
			if (url.indexOf("?") > 0) {
				url = url + "&" + getprams;
			} else {
				url = url + "?" + getprams;
			}
		}
	}

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

package cn.emay.sdk.util.http.response.impl.string;

import java.util.List;
import java.util.Map;

import cn.emay.sdk.util.http.common.EmayHttpResultCode;

/**
 * 【自定义】String类型Http响应
 * 
 * @author Frank
 *
 */
public class EmayHttpResponseBytes {

	private EmayHttpResultCode resultCode; //HttpClient 结果代码
	private int httpCode; //Http链接Code
	private Map<String, String> headers;//Http响应头
	private List<String> cookies;//http响应Cookies
	private byte[] resultBytes;//http响应数据
	private String charSet;//http响应编码

	/**
	 * 
	 * @param charSet http响应编码
	 * @param resultCode HttpClient结果代码
	 * @param httpCode Http链接Code
	 * @param headers Http响应头
	 * @param cookies http响应Cookies
	 * @param resultString http响应数据
	 */
	public EmayHttpResponseBytes(String charSet,EmayHttpResultCode resultCode, int httpCode, Map<String, String> headers, List<String> cookies, byte[] resultBytes) {
		this.resultCode = resultCode;
		this.httpCode = httpCode;
		this.headers = headers;
		this.cookies = cookies;
		this.resultBytes = resultBytes;
		this.charSet = charSet;
	}

	public EmayHttpResultCode getResultCode() {
		return resultCode;
	}

	public void setResultCode(EmayHttpResultCode resultCode) {
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

	public byte[] getResultBytes() {
		return resultBytes;
	}

	public void setResultBytes(byte[] resultBytes) {
		this.resultBytes = resultBytes;
	}
	
}

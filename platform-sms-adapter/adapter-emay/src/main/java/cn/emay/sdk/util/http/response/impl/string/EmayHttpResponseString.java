package cn.emay.sdk.util.http.response.impl.string;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import cn.emay.sdk.util.http.common.EmayHttpResultCode;

/**
 * 【自定义】String类型Http响应
 * 
 * @author Frank
 *
 */
public class EmayHttpResponseString {

	private EmayHttpResultCode resultCode; //HttpClient 结果代码
	private int httpCode; //Http链接Code
	private Map<String, String> headers;//Http响应头
	private List<String> cookies;//http响应Cookies
	private String resultString;//http响应数据
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
	public EmayHttpResponseString(String charSet,EmayHttpResultCode resultCode, int httpCode, Map<String, String> headers, List<String> cookies, String resultString) {
		this.resultCode = resultCode;
		this.httpCode = httpCode;
		this.headers = headers;
		this.cookies = cookies;
		this.resultString = resultString;
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

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for(Field filed : this.getClass().getDeclaredFields()){
			Object value = null;
			try {
				value = filed.get(this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			buffer.append("[ ").append(filed.getName()).append(" : ").append(value).append(" ]");
		}
		return buffer.toString();
		
	}

	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	
}

package cn.emay.util.http;

import java.util.List;
import java.util.Map;

/**
 * http 响应: String
 * 
 * @author Frank
 *
 */
public class HttpResponseString extends HttpResponse<String> {

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
	public HttpResponseString(HttpResultCode resultCode, int httpCode, Map<String, String> headers, List<String> cookies, String charSet, String result) {
		super(resultCode, httpCode, headers, cookies, charSet, result);
	}


}

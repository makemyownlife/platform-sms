package cn.emay.util.http;

import java.util.Map;

/**
 * Http 请求实体<Map<String, String>>
 * 
 * @author Frank
 *
 */
public class HttpRequestKV extends HttpRequest<Map<String, String>> {

	/**
	 * 
	 * @param httpParams
	 *            请求参数
	 */
	public HttpRequestKV(HttpRequestParams<Map<String, String>> httpParams) {
		super(httpParams, new HttpRequestPraserKV());
	}


}

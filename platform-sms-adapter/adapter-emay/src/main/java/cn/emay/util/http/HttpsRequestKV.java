package cn.emay.util.http;

import java.util.Map;

/**
 * Https 请求实体<Map<String, String>>
 * 
 * @author Frank
 *
 */
public class HttpsRequestKV extends HttpRequest<Map<String, String>> {

	/**
	 * 
	 * @param httpParams
	 *            http请求参数
	 * @param httpsParams
	 *            https参数
	 */
	public HttpsRequestKV(HttpRequestParams<Map<String, String>> httpParams, HttpsParams httpsParams) {
		super(httpParams, httpsParams, new HttpRequestPraserKV());
	}

}

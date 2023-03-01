package cn.emay.util.http;

/**
 * Https 请求实体<String>
 * 
 * @author Frank
 *
 */
public class HttpsRequestString extends HttpRequest<String> {

	/**
	 * 
	 * @param httpParams
	 *            http请求参数
	 * @param httpsParams
	 *            https参数
	 */
	public HttpsRequestString(HttpRequestParams<String> httpParams, HttpsParams httpsParams) {
		super(httpParams, httpsParams, new HttpRequestPraserString());
	}

}

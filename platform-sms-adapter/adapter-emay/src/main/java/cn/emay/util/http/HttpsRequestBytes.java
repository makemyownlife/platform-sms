package cn.emay.util.http;

/**
 * Https 请求实体<byte[]>
 * 
 * @author Frank
 *
 */
public class HttpsRequestBytes extends HttpRequest<byte[]> {

	/**
	 * 
	 * @param httpParams
	 *            http请求参数
	 * @param httpsParams
	 *            https参数
	 */
	public HttpsRequestBytes(HttpRequestParams<byte[]> httpParams, HttpsParams httpsParams) {
		super(httpParams, httpsParams, new HttpRequestPraserBytes());
	}

}

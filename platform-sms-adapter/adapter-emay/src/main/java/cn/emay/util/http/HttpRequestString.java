package cn.emay.util.http;

/**
 * Http 请求实体<String>
 * 
 * @author Frank
 *
 */
public class HttpRequestString extends HttpRequest<String> {

	/**
	 * 
	 * @param httpParams
	 *            请求参数
	 */
	public HttpRequestString(HttpRequestParams<String> httpParams) {
		super(httpParams, new HttpRequestPraserString());
	}

}

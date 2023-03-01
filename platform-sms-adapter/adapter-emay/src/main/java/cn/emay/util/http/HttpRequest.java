package cn.emay.util.http;

/**
 * Http 请求实体<请求数据类型>
 * 
 * @author Frank
 *
 * @param <T>
 */
public class HttpRequest<T> {

	/**
	 * http参数
	 */
	private HttpRequestParams<T> httpParams;

	/**
	 * https参数
	 */
	private HttpsParams httpsParams;

	/**
	 * 内容解析器
	 */
	private HttpRequestPraser<T> contentPraser;

	/**
	 * 是否https请求
	 */
	private boolean isHttps;

	/**
	 * 
	 */
	protected HttpRequest() {

	}

	/**
	 * 
	 * @param httpParams
	 *            http参数
	 * @param contentPraser
	 *            内容解析器
	 */
	protected HttpRequest(HttpRequestParams<T> httpParams, HttpRequestPraser<T> contentPraser) {
		this.httpParams = httpParams;
		this.contentPraser = contentPraser;
		this.isHttps = false;
	}

	/**
	 * 
	 * @param httpParams
	 *            http参数
	 * @param httpsParams
	 *            https参数
	 * @param contentPraser
	 *            内容解析器
	 */
	protected HttpRequest(HttpRequestParams<T> httpParams, HttpsParams httpsParams, HttpRequestPraser<T> contentPraser) {
		this.httpParams = httpParams;
		this.httpsParams = httpsParams;
		this.contentPraser = contentPraser;
		this.isHttps = true;
	}

	public boolean isHttps() {
		return isHttps;
	}

	public HttpRequestParams<T> getHttpParams() {
		return httpParams;
	}

	public void setHttpParams(HttpRequestParams<T> httpParams) {
		this.httpParams = httpParams;
	}

	public HttpsParams getHttpsParams() {
		return httpsParams;
	}

	public void setHttpsParams(HttpsParams httpsParams) {
		this.httpsParams = httpsParams;
	}

	public HttpRequestPraser<T> getContentPraser() {
		return contentPraser;
	}

	public void setContentPraser(HttpRequestPraser<T> contentPraser) {
		this.contentPraser = contentPraser;
	}

}

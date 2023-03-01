package cn.emay.util.http;

/**
 * Http请求，参数解析器
 * 
 * @author Frank
 *
 * @param <T>
 */
public interface HttpRequestPraser<T> {

	/**
	 * 将请求参数转换为String<br/>
	 * 主要用于get方法传输
	 * 
	 * @param httpParams
	 *            请求参数
	 * @return
	 */
	public String praseRqeuestContentToString(HttpRequestParams<T> httpParams);

	/**
	 * 将请求参数转换为byte[]<br/>
	 * 主要用于post方法传输
	 * 
	 * @param httpParams
	 *            请求参数
	 * @return
	 */
	public byte[] praseRqeuestContentToBytes(HttpRequestParams<T> httpParams);

	/**
	 * 获取请求参数大小<br/>
	 * 主要用于post方法传输
	 * 
	 * @param httpParams
	 *            请求参数
	 * @return
	 */
	public int praseRqeuestContentLength(HttpRequestParams<T> httpParams);

}

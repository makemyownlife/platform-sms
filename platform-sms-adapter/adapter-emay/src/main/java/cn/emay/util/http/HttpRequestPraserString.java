package cn.emay.util.http;

import java.io.UnsupportedEncodingException;

/**
 * Http 请求解析器：String
 * 
 * @author Frank
 *
 */
public class HttpRequestPraserString implements HttpRequestPraser<String> {

	/**
	 * 请求内容byte数组
	 */
	private byte[] contentBytes;

	@Override
	public String praseRqeuestContentToString(HttpRequestParams<String> httpParams) {
		return httpParams.getParams();
	}

	@Override
	public byte[] praseRqeuestContentToBytes(HttpRequestParams<String> httpParams) {
		if (contentBytes != null) {
			return contentBytes;
		}
		try {
			contentBytes = httpParams.getParams().getBytes(httpParams.getCharSet());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			contentBytes = null;
		}
		return contentBytes;
	}

	@Override
	public int praseRqeuestContentLength(HttpRequestParams<String> httpParams) {
		praseRqeuestContentToBytes(httpParams);
		if (contentBytes != null) {
			return contentBytes.length;
		} else {
			return 0;
		}
	}

}

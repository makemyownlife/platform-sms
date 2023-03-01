package cn.emay.util.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Http 请求解析器：Map<String, String>
 * 
 * @author Frank
 *
 */
public class HttpRequestPraserKV implements HttpRequestPraser<Map<String, String>> {

	/**
	 * 请求内容byte数组
	 */
	private byte[] contentBytes;

	/**
	 * 请求内容字符串
	 */
	private String contentString;

	@Override
	public String praseRqeuestContentToString(HttpRequestParams<Map<String, String>> httpParams) {
		if (contentString != null) {
			return contentString;
		}
		Map<String, String> params = httpParams.getParams();
		if (params == null || params.size() == 0) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, String> entry : params.entrySet()) {
			if (entry.getValue() != null) {
				buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		String param = buffer.toString();
		contentString = param.substring(0, param.length() - 1);
		return contentString;
	}

	@Override
	public byte[] praseRqeuestContentToBytes(HttpRequestParams<Map<String, String>> httpParams) {
		if (contentBytes != null) {
			return contentBytes;
		}
		String paramStr = praseRqeuestContentToString(httpParams);
		if (paramStr == null) {
			return null;
		}
		try {
			contentBytes = paramStr.getBytes(httpParams.getCharSet());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return contentBytes;
	}

	@Override
	public int praseRqeuestContentLength(HttpRequestParams<Map<String, String>> httpParams) {
		praseRqeuestContentToBytes(httpParams);
		if (contentBytes != null) {
			return contentBytes.length;
		} else {
			return 0;
		}
	}

}

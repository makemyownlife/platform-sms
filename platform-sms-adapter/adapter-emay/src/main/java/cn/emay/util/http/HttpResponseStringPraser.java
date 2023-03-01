package cn.emay.util.http;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 解析String响应的解析器
 * 
 * @author Frank
 *
 */
public class HttpResponseStringPraser implements HttpResponsePraser<HttpResponseString>{

	@Override
	public HttpResponseString prase(HttpResultCode resultCode, int httpCode, Map<String, String> headers, List<String> cookies, String charSet, ByteArrayOutputStream outputStream) {
		String st = null;
		try {
			if(outputStream != null){
				byte[] resultBytes = outputStream.toByteArray();
				st = new String(resultBytes, charSet);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new HttpResponseString(resultCode, httpCode, headers, cookies, charSet, st);
	}

	
}

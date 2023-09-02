package cn.emay.sdk.util.http.request.impl;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Map.Entry;

import cn.emay.sdk.util.http.request.EmayHttpRequest;

/**
 * 传输数据为Key-Value的请求实体
 * 
 * @author Frank
 *
 */
public class EmayHttpRequestKV extends EmayHttpRequest<Map<String, String>> {

	public EmayHttpRequestKV(String url, String charSet, String method, Map<String, String> headers, String cookies, Map<String, String> params) {
		super(url, charSet, method, headers, cookies, params);
	}

	@Override
	public byte[] paramsToBytesForPost() {
		String paramStr = paramsToStringForGet();
		if(paramStr == null){
			return null;
		}
		byte[] param = null;
		try {
			param = paramStr.getBytes(this.getCharSet());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return param;
	}

	@Override
	public String paramsToStringForGet() {
		Map<String, String> params = this.getParams();
		if (params == null || params.size() == 0) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (Entry<String, String> entry : params.entrySet()) {
			if(entry.getValue() != null){
				buffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		String param = buffer.toString();
		param = param.substring(0,param.length() - 1);
		return param;
	}
	
}

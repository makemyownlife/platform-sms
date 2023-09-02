package cn.emay.sdk.util;

import java.util.HashMap;
import java.util.Map;

import cn.emay.sdk.common.CommonConstants;
import cn.emay.sdk.core.dto.sms.common.ResultModel;
import cn.emay.sdk.util.http.client.EmayHttpClient;
import cn.emay.sdk.util.http.common.EmayHttpResultCode;
import cn.emay.sdk.util.http.request.impl.EmayHttpRequestBytes;
import cn.emay.sdk.util.http.request.impl.EmayHttpRequestKV;
import cn.emay.sdk.util.http.response.impl.string.EmayHttpResponseBytes;
import cn.emay.sdk.util.http.response.impl.string.EmayHttpResponseBytesPraser;
import cn.emay.sdk.util.http.response.impl.string.EmayHttpResponseString;
import cn.emay.sdk.util.http.response.impl.string.EmayHttpResponseStringPraser;
import cn.emay.sdk.util.json.JsonHelper;

public class HttpUtil {

	private static String remoteSign = "SDK";

	public static <T> ResultModel<T> request(String appId, String secretKey, String url, Object object, Class<T> clazz) {
		String code = "SYSTEM";// 默认系统异常
		T result = null;
		EmayHttpRequestBytes request = null;
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("appId", appId);
			headers.put("encode", "UTF-8");
			headers.put("gzip", "on");
			headers.put("remoteSign", remoteSign);
			byte[] bytes = JsonHelper.toJsonString(object).getBytes("UTF-8");
			bytes = GZIPUtils.compress(bytes);
			byte[] parambytes = AES.encrypt(bytes, secretKey.getBytes(), CommonConstants.algorithm);
			request = new EmayHttpRequestBytes(url, "UTF-8", "POST", headers, null, parambytes);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel<T>(code, result);
		}
		try {
			EmayHttpClient client = new EmayHttpClient();
			EmayHttpResponseBytes res = client.service(request, new EmayHttpResponseBytesPraser());
			if (res == null) {
				return new ResultModel<T>(code, result);
			}
			if (res.getResultCode().equals(EmayHttpResultCode.SUCCESS)) {
				if (res.getHttpCode() == 200) {
					code = res.getHeaders().get("result");
					if (code.equals("SUCCESS")) {
						byte[] data = res.getResultBytes();
						data = AES.decrypt(data, secretKey.getBytes(), CommonConstants.algorithm);
						data = GZIPUtils.decompress(data);
						String json = new String(data, "UTF-8");
						result = JsonHelper.fromJson(clazz, json);
					}
				} else {
					return new ResultModel<T>(code, result);
				}
			} else {
				return new ResultModel<T>(code, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultModel<T>(code, result);
		}
		return new ResultModel<T>(code, result);
	}

	public static <T> ResultModel<T> request(Map<String, String> params, String url, String encode, Class<T> clazz) {
		String code = "SYSTEM";// 默认系统异常
		T result = null;
		EmayHttpRequestKV request = new EmayHttpRequestKV(url, encode, "POST", null, null, params);
		EmayHttpClient client = new EmayHttpClient();
		String json = null;
		try {
			String mapst = "";
			for (String key : params.keySet()) {
				String value = params.get(key);
				mapst += key + "=" + value + "&";
			}
			mapst = mapst.substring(0, mapst.length() - 1);
			EmayHttpResponseString res = client.service(request, new EmayHttpResponseStringPraser());
			if (res == null) {
				return new ResultModel<T>(code, result);
			}
			if (res.getResultCode().equals(EmayHttpResultCode.SUCCESS)) {
				if (res.getHttpCode() == 200) {
					code = res.getResultCode().getCode();
					json = res.getResultString();
					result = JsonHelper.fromJson(clazz, json);
				} else {
					return new ResultModel<T>(code, result);
				}
			} else {
				return new ResultModel<T>(code, result);
			}
		} catch (Exception e) {
			return new ResultModel<T>(code, result);
		}
		return new ResultModel<T>(code, result);
	}

}

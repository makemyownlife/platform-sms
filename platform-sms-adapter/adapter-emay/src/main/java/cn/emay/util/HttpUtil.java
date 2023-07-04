package cn.emay.util;

import cn.emay.ResultModel;
import cn.emay.util.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyong on 2023/7/4.
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static ResultModel request(String appId, String secretKey, String algorithm, Object content, String url, final boolean isGzip, String encode) {
        Map<String, String> headers = new HashMap<String, String>();
        HttpRequest<byte[]> request = null;
        try {
            headers.put("appId", appId);
            headers.put("encode", encode);
            String requestJson = JsonHelper.toJsonString(content);
            logger.info("result json: " + requestJson);
            byte[] bytes = requestJson.getBytes(encode);
            logger.info("request data size : " + bytes.length);
            if (isGzip) {
                headers.put("gzip", "on");
                bytes = GZIPUtils.compress(bytes);
                logger.info("request data size [com]: " + bytes.length);
            }
            byte[] parambytes = AES.encrypt(bytes, secretKey.getBytes(), algorithm);
            logger.info("request data size [en] : " + parambytes.length);
            HttpRequestParams<byte[]> params = new HttpRequestParams<byte[]>();
            params.setCharSet("UTF-8");
            params.setMethod("POST");
            params.setHeaders(headers);
            params.setParams(parambytes);
            params.setUrl(url);
            if (url.startsWith("https://")) {
                request = new HttpsRequestBytes(params, null);
            } else {
                request = new HttpRequestBytes(params);
            }
        } catch (Exception e) {
            logger.error("加密异常:", e);
        }
        HttpClient client = new HttpClient();
        String code = null;
        String result = null;
        try {
            HttpResponseBytes res = client.service(request, new HttpResponseBytesPraser());
            if (res == null) {
                return new ResultModel(code, result);
            }
            if (res.getResultCode().equals(HttpResultCode.SUCCESS)) {
                if (res.getHttpCode() == 200) {
                    code = res.getHeaders().get("result");
                    if (code.equals("SUCCESS")) {
                        byte[] data = res.getResult();
                        logger.info("response data size [en and com] : " + data.length);
                        data = AES.decrypt(data, secretKey.getBytes(), algorithm);
                        if (isGzip) {
                            data = GZIPUtils.decompress(data);
                        }
                        logger.info("response data size : " + data.length);
                        result = new String(data, encode);
                        logger.info("response json: " + result);
                    }
                } else {
                    logger.info("请求接口异常,请求码:" + res.getHttpCode());
                }
            } else {
                logger.info("请求接口网络异常:" + res.getResultCode().getCode());
            }
        } catch (Exception e) {
            logger.info("解析失败:", e);
        }
        ResultModel re = new ResultModel(code, result);
        return re;
    }
}

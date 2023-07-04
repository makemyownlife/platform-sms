package com.courage.platform.sms.adapter.emay.test;

import cn.emay.ResultModel;
import cn.emay.eucp.inter.framework.dto.TemplateSmsIdAndMobile;
import cn.emay.eucp.inter.http.v1.dto.request.TemplateSmsSendRequest;
import cn.emay.eucp.inter.http.v1.dto.response.SmsResponse;
import cn.emay.util.AES;
import cn.emay.util.GZIPUtils;
import cn.emay.util.JsonHelper;
import cn.emay.util.http.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EmayUnitTest {

    @Test
    public void sendTemplate() {
        String appId = "EUCP-EMY-SMS1-0BIGB";// 请联系销售，或者在页面中 获取
        // 密钥
        String secretKey = "69C5DCC5531E15BF";// 请联系销售，或者在页面中 获取
        // 接口地址
        String host = "http://www.btom.cn:8080"; // 请联系销售获取
        // 加密算法
        String algorithm = "AES/ECB/PKCS5Padding";
        // 编码
        String encode = "UTF-8";
        // 是否压缩
        boolean isGizp = true;
        String templateId = "22222";
        // 扩展 code
        String extendedCode = "111";
        TemplateSmsSendRequest pamars = new TemplateSmsSendRequest();
        TemplateSmsIdAndMobile[] customSmsIdAndMobiles =
                new TemplateSmsIdAndMobile[]{new TemplateSmsIdAndMobile("15011319235", "1"),
                        new TemplateSmsIdAndMobile("17607197036", "2")};
        pamars.setSmses(customSmsIdAndMobiles);
        pamars.setTemplateId(templateId);
        pamars.setExtendedCode(extendedCode);
        pamars.setRequestTime(System.currentTimeMillis());
        System.out.println(JsonHelper.toJsonString(pamars));
        ResultModel result = request(appId, secretKey, algorithm, pamars, host + "/inter/sendTemplateNormalSMS", isGizp, encode);
        System.out.println("result code :" + result.getCode());
        if ("SUCCESS".equals(result.getCode())) {
            SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
            if (response != null) {
                for (SmsResponse d : response) {
                    System.out.println("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
                }
            }
        }
    }

    @Test
    public void addTemplate() {
        String appId = "EUCP-EMY-SMS1-0BIGB";// 请联系销售，或者在页面中 获取
        // 密钥
        String secretKey = "69C5DCC5531E15BF";// 请联系销售，或者在页面中 获取
        // 接口地址
        String host = "http://www.btom.cn:8080"; // 请联系销售获取
        // 加密算法
        String algorithm = "AES/ECB/PKCS5Padding";
        // 编码
        String encode = "UTF-8";
        // 是否压缩
        boolean isGizp = true;
        // 模板创建
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("templateContent", "ssssssssss${参数1}dddddddddddddd${参数2}dddddddddd");
        templateMap.put("requestTime", String.valueOf(System.currentTimeMillis()));
        templateMap.put("requestValidPeriod", "30");
        ResultModel result = request(appId, secretKey, algorithm, templateMap, host + "/inter/createTemplateSMS", isGizp, encode);
        System.out.println("result code :" + result.getCode());
        if ("SUCCESS".equals(result.getCode())) {
            System.out.println(result.getResult());
        }
        System.out.println("=============end createTemplateSMS==================");
    }

    public static ResultModel request(String appId, String secretKey, String algorithm, Object content, String url, final boolean isGzip, String encode) {
        Map<String, String> headers = new HashMap<String, String>();
        HttpRequest<byte[]> request = null;
        try {
            headers.put("appId", appId);
            headers.put("encode", encode);
            String requestJson = JsonHelper.toJsonString(content);
            System.out.println("result json: " + requestJson);
            byte[] bytes = requestJson.getBytes(encode);
            System.out.println("request data size : " + bytes.length);
            if (isGzip) {
                headers.put("gzip", "on");
                bytes = GZIPUtils.compress(bytes);
                System.out.println("request data size [com]: " + bytes.length);
            }
            byte[] parambytes = AES.encrypt(bytes, secretKey.getBytes(), algorithm);
            System.out.println("request data size [en] : " + parambytes.length);
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
            System.out.println("加密异常");
            e.printStackTrace();
        }
        HttpClient client = new HttpClient();
        String code = null;
        String result = null;
        try {
            HttpResponseBytes res = client.service(request, new HttpResponseBytesPraser());
            if (res == null) {
                System.out.println("请求接口异常");
                return new ResultModel(code, result);
            }
            if (res.getResultCode().equals(HttpResultCode.SUCCESS)) {
                if (res.getHttpCode() == 200) {
                    code = res.getHeaders().get("result");
                    if (code.equals("SUCCESS")) {
                        byte[] data = res.getResult();
                        System.out.println("response data size [en and com] : " + data.length);
                        data = AES.decrypt(data, secretKey.getBytes(), algorithm);
                        if (isGzip) {
                            data = GZIPUtils.decompress(data);
                        }
                        System.out.println("response data size : " + data.length);
                        result = new String(data, encode);
                        System.out.println("response json: " + result);
                    }
                } else {
                    System.out.println("请求接口异常,请求码:" + res.getHttpCode());
                }
            } else {
                System.out.println("请求接口网络异常:" + res.getResultCode().getCode());
            }
        } catch (Exception e) {
            System.out.println("解析失败");
            e.printStackTrace();
        }
        ResultModel re = new ResultModel(code, result);
        return re;
    }

}

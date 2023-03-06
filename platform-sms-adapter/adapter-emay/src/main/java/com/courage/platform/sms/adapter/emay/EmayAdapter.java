package com.courage.platform.sms.adapter.emay;

import cn.emay.ResultModel;
import cn.emay.eucp.inter.framework.dto.TemplateSmsIdAndMobile;
import cn.emay.eucp.inter.http.v1.dto.request.TemplateSmsSendRequest;
import cn.emay.eucp.inter.http.v1.dto.response.SmsResponse;
import cn.emay.util.AES;
import cn.emay.util.GZIPUtils;
import cn.emay.util.JsonHelper;
import cn.emay.util.http.*;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.send.SmsAdapterRequest;
import com.courage.platform.sms.adapter.send.SmsAdapterResponse;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SPI("emay")
public class EmayAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(EmayAdapter.class);

    private final static String algorithm = "AES/ECB/PKCS5Padding";

    private final static boolean isGizp = true;

    private final static String encode = "UTF-8";

    private final static String extendCode = "111";

    private SmsChannelConfig smsChannelConfig;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) {
        logger.info("初始化亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppKey() + "]");
        this.smsChannelConfig = smsChannelConfig;
    }

    @Override
    public SmsAdapterResponse sendSmsByTemplateId(SmsAdapterRequest smsSendRequest) {
        TemplateSmsSendRequest pamars = new TemplateSmsSendRequest();
        String[] mobiles = StringUtils.split(smsSendRequest.getPhoneNumbers(), ",");
        List<TemplateSmsIdAndMobile> smsIdAndMobilesList = new ArrayList(mobiles.length);
        for (int i = 0; i < mobiles.length; i++) {
            TemplateSmsIdAndMobile templateSmsIdAndMobile = new TemplateSmsIdAndMobile(mobiles[i], UUID.randomUUID().toString().replaceAll("-", ""));
            smsIdAndMobilesList.add(templateSmsIdAndMobile);
        }
        pamars.setTemplateId(smsSendRequest.getTemplateCode());
        pamars.setExtendedCode(extendCode);
        pamars.setRequestTime(System.currentTimeMillis());
        pamars.setTimerTime(smsSendRequest.getTimerTime());
        logger.info(JsonHelper.toJsonString(pamars));
        ResultModel result = request(
                smsChannelConfig.getChannelAppKey(),
                smsChannelConfig.getChannelAppSecret(),
                algorithm,
                pamars,
                smsChannelConfig.getChannelDomain() + "/inter/sendTemplateNormalSMS",
                isGizp,
                encode);
        if ("SUCCESS".equals(result.getCode())) {
            SmsResponse[] response = JsonHelper.fromJson(SmsResponse[].class, result.getResult());
            if (response != null) {
                for (SmsResponse d : response) {
                    logger.info("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
                }
                return new SmsAdapterResponse(SmsAdapterResponse.SUCCESS_CODE, result.getResult());
            }
        }
        return new SmsAdapterResponse(SmsAdapterResponse.FAIL_CODE, result.getResult());
    }

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
            e.printStackTrace();
        }
        ResultModel re = new ResultModel(code, result);
        return re;
    }

    @Override
    public void destroy() {
    }

}

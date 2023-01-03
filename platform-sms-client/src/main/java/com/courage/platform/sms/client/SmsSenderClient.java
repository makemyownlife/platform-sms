package com.courage.platform.sms.client;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.client.util.SmsHttpClientUtils;
import com.courage.platform.sms.client.util.SmsSenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送客户端
 * Created by zhangyong on 2020/1/5.
 */
public class SmsSenderClient {

    private final static Logger logger = LoggerFactory.getLogger(SmsSenderClient.class);

    private SmsConfig smsConfig;

    public SmsSenderClient(SmsConfig smsConfig) {
        this.smsConfig = smsConfig;
    }

    public SmsSenderResult sendSingle(String mobile, String content) throws IOException {
        Long random = SmsSenderUtil.getRandom();
        String appKey = smsConfig.getAppKey();
        Long time = SmsSenderUtil.getCurrentTime();
        String sign = SmsSenderUtil.calculateSignature(smsConfig.getAppSecret(), random, time);
        // 构造参数
        Map<String, String> param = new HashMap<String, String>(4);
        param.put("time", String.valueOf(time));
        param.put("appKey", appKey);
        param.put("random", String.valueOf(random));
        param.put("sign", sign);
        Map<String, String> queryParam = new HashMap<String, String>(4);
        queryParam.put("mobile", mobile);
        queryParam.put("content", content);
        param.put("q", JSON.toJSONString(queryParam));
        // 发送请求
        try {
            String result = SmsHttpClientUtils.doPost(smsConfig.getSmsServerUrl(), param, 5000, 5000);
            SmsSenderResult senderResult = JSON.parseObject(result, SmsSenderResult.class);
            return senderResult;
        } catch (Exception e) {
            logger.error("smsClient sendSingle error:", e);
            return new SmsSenderResult(SmsSenderResult.FAIL_CODE, "系统异常");
        }
    }

}

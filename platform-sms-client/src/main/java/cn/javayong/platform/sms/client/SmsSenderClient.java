package cn.javayong.platform.sms.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.javayong.platform.sms.client.util.LoadBalancer;
import cn.javayong.platform.sms.client.util.SmsSenderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送客户端
 * Created by zhangyong on 2020/1/5.
 */
public class SmsSenderClient {

    private final static Logger logger = LoggerFactory.getLogger(SmsSenderClient.class);

    private final static String SINGLE_SEND_REQUEST_URL = "/gateway/sendByTemplateId";

    private SmsConfig smsConfig;

    private LoadBalancer loadBalancer;

    public SmsSenderClient(SmsConfig smsConfig) {
        this.smsConfig = smsConfig;
        this.loadBalancer = new LoadBalancer(smsConfig.getSmsServerUrl());
    }

    public SmsSenderResult sendSmsByTemplateId(String mobile, String templateId, Map<String, String> templateParam) {
        String random = SmsSenderUtil.getRandom();
        String appKey = smsConfig.getAppKey();
        String time = String.valueOf(SmsSenderUtil.getCurrentTime());
        // 构造参数
        Map<String, String> param = new HashMap<String, String>(4);
        param.put("time", time);
        param.put("appKey", appKey);
        param.put("random", random);
        Map<String, Object> queryParam = new HashMap<String, Object>(4);
        queryParam.put("mobile", mobile);
        queryParam.put("templateId", templateId);
        queryParam.put("templateParam", templateParam);
        String q = JSON.toJSONString(queryParam);
        param.put("q", JSON.toJSONString(queryParam));
        String sign = SmsSenderUtil.calculateSignature(smsConfig.getAppSecret(), random, time, q);
        param.put("sign", sign);
        // 发送请求
        try {
            JSONObject jsonResult = loadBalancer.doSendRequest(SINGLE_SEND_REQUEST_URL, param);
            if (jsonResult != null) {
                SmsSenderResult senderResult = jsonResult.toJavaObject(SmsSenderResult.class);
                return senderResult;
            }
        } catch (Exception e) {
            logger.error("smsClient sendSingle error:", e);
        }
        return new SmsSenderResult(SmsSenderResult.FAIL_CODE, "系统异常");
    }

}

package com.courage.platform.sms.client.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡工具类
 */
public class LoadBalancer {

    private final static Logger logger = LoggerFactory.getLogger(LoadBalancer.class);

    private static List<String> CURRENT_SMS_NODES = new ArrayList<String>();

    private static AtomicInteger INDEX = new AtomicInteger(0);

    public LoadBalancer(String smsServerUrl) {
        String[] arr = StringUtils.split(smsServerUrl, ";");
        CURRENT_SMS_NODES.addAll(Arrays.asList(arr));
    }

    public JSONObject doSendRequest(String requestPath, Map<String, String> params) {
        int size = CURRENT_SMS_NODES.size();
        int tryCount = (size >= 2) ? 2 : 1;
        JSONObject jsonResult = null;
        for (int i = 0; i < tryCount; i++) {
            String nodeUrl = CURRENT_SMS_NODES.get(Math.abs(INDEX.intValue() % size));
            try {
                String httpResult = SmsHttpClientUtils.doPost(nodeUrl + requestPath, params, 5000, 5000);
                jsonResult = JSON.parseObject(httpResult);
                int code = jsonResult.getIntValue("code");
                if (code == 200) {
                    return jsonResult;
                }
            } catch (Exception e) {
                logger.error("doSendRequest error:", e);
            } finally {
                INDEX.incrementAndGet();
            }
        }
        return jsonResult;
    }
}

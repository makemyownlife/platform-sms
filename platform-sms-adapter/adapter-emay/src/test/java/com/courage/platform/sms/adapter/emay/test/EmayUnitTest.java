package com.courage.platform.sms.adapter.emay.test;

import cn.emay.sdk.client.SmsSDKClient;
import cn.emay.sdk.core.dto.sms.common.ResultModel;
import cn.emay.sdk.core.dto.sms.request.SmsSingleRequest;
import cn.emay.sdk.core.dto.sms.response.SmsResponse;
import cn.emay.sdk.util.exception.SDKParamsException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EmayUnitTest {

    @Test
    public void sendSingleSMS() throws SDKParamsException {
        String appId = "xxxx";// 请联系销售，或者在页面中 获取
        // 密钥
        String secretKey = "xxx";// 请联系销售，或者在页面中 获取
        // 接口地址
        String host = "http://www.btom.cn"; // 请联系销售获取
        SmsSDKClient client = new SmsSDKClient(host, 8080, appId, secretKey);
        String mobile = "15011319235";
        String content = "【张勇的博客】短信内容";
        String customSmsId = "1";
        String extendedCode = "01";
        SmsSingleRequest request = new SmsSingleRequest(mobile, content, customSmsId, extendedCode, "");
        ResultModel<SmsResponse> result = client.sendSingleSms(request);
        if (result.getCode().equals("SUCCESS")) {
            System.out.println("请求成功");
            SmsResponse response = result.getResult();
            System.out.println("sendSingleSms:" + response.toString());
        } else {
            System.out.println("请求失败");
        }
    }


}

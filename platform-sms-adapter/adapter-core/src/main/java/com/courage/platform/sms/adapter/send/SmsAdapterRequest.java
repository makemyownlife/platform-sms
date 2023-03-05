package com.courage.platform.sms.adapter.send;

/**
 * 短信发送请求(适配器)
 * Created by zhangyong on 2023/3/1.
 */
public class SmsAdapterRequest {

    // 手机号 (逗号分隔)
    private String phoneNumbers;

    // 模版编码
    private String templateCode;

    // 模版参数
    public String templateParam;

    // 签名
    public String signName;

    // 请求编号
    public String requestId;

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}

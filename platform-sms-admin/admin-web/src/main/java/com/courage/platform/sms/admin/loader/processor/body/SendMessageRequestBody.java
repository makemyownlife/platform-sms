package com.courage.platform.sms.admin.loader.processor.body;

import com.courage.platform.sms.admin.loader.processor.ProcessorRequestBody;

public class SendMessageRequestBody extends ProcessorRequestBody {

    private String mobile;

    private String templateId;

    private String appId;

    private String templateParam;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

}

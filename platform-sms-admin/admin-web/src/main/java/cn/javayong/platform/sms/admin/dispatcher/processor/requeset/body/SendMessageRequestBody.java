package cn.javayong.platform.sms.admin.dispatcher.processor.requeset.body;

import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestBody;

public class SendMessageRequestBody extends RequestBody {

    private String mobile;

    private String templateId;

    private Long appId;

    private String templateParam;

    private String attime;

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

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getTemplateParam() {
        return templateParam;
    }

    public void setTemplateParam(String templateParam) {
        this.templateParam = templateParam;
    }

    public String getAttime() {
        return attime;
    }

    public void setAttime(String attime) {
        this.attime = attime;
    }

}


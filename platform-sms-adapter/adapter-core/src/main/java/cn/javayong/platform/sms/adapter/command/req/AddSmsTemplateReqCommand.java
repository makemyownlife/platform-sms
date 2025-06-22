package cn.javayong.platform.sms.adapter.command.req;

/**
 * 添加短信模版请求
 * Created by zhangyong on 2023/7/4.
 */
public class AddSmsTemplateReqCommand extends SmsReqCommand {

    public String signName;

    public String templateContent;

    public String templateName;

    public Integer templateType;

    public String remark;

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Integer getTemplateType() {
        return templateType;
    }

    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

}

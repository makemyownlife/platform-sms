package cn.javayong.platform.sms.adapter.command.req;

/**
 * 短信发送请求(适配器)
 * Created by zhangyong on 2023/3/1.
 */
public class SendSmsReqCommand extends SmsRequestCommand {

    // 手机号 (逗号分隔)
    private String phoneNumbers;

    // 模版编码
    private String templateCode;

    // 标准模版内容
    private String templateContent;

    /**
     * 模版参数 : 短信模板变量对应的实际值。支持传入多个参数，示例：{"name":"张三","number":"1390000****"}。
     */
    private String templateParam;

    // 签名
    private String signName;

    // 定时发送时间
    private String timerTime;

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

    public String getTimerTime() {
        return timerTime;
    }

    public void setTimerTime(String timerTime) {
        this.timerTime = timerTime;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

}

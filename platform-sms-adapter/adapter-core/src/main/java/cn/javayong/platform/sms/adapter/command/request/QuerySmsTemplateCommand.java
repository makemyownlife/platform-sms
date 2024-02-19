package cn.javayong.platform.sms.adapter.command.request;

/**
 * 查询短信
 * Created by zhangyong on 2023/9/6.
 */
public class QuerySmsTemplateCommand {

    private String templateCode;

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

}

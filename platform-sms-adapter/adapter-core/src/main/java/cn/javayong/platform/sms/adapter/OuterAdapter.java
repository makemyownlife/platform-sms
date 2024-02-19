package cn.javayong.platform.sms.adapter;


import cn.javayong.platform.sms.adapter.command.request.AddSmsTemplateCommand;
import cn.javayong.platform.sms.adapter.command.request.QuerySmsTemplateCommand;
import cn.javayong.platform.sms.adapter.command.request.SendSmsCommand;
import cn.javayong.platform.sms.adapter.support.SPI;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import cn.javayong.platform.sms.adapter.command.response.SmsResponseCommand;

import java.util.Map;

/**
 * 外部适配器接口
 */
@SPI("aliyun")
public interface OuterAdapter {

    /**
     * 适配器初始化接口
     */
    void init(SmsChannelConfig smsChannelConfig) throws Exception;

    /**
     * 按照模版编号发送短信
     */
    SmsResponseCommand<String> sendSmsByTemplateId(SendSmsCommand smsSendRequest);

    /**
     * 按照模版编号发送短信
     */
    SmsResponseCommand<Map<String, String>> addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand);

    /**
     * 按照模版编号查询模版状态
     */
    SmsResponseCommand<Integer> querySmsTemplateStatus(QuerySmsTemplateCommand querySmsTemplateCommand);

    /**
     * 外部适配器销毁接口
     */
    void destroy();

}

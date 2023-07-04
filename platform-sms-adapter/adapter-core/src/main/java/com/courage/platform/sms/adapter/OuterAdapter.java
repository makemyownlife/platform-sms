package com.courage.platform.sms.adapter;


import com.courage.platform.sms.adapter.command.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.SendSmsCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;

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
    SmsResponseCommand sendSmsByTemplateId(SendSmsCommand smsSendRequest);

    /**
     * 按照模版编号发送短信
     */
    SmsResponseCommand addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand);

    /**
     * 外部适配器销毁接口
     */
    void destroy();

}

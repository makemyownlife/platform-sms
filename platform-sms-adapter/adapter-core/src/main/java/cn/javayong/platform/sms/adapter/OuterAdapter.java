package cn.javayong.platform.sms.adapter;


import cn.javayong.platform.sms.adapter.command.req.AddSmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.QuerySmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.SendSmsReqCommand;
import cn.javayong.platform.sms.adapter.support.SPI;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import cn.javayong.platform.sms.adapter.command.resp.SmsRespCommand;

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
    SmsRespCommand<String> sendSmsByTemplateId(SendSmsReqCommand smsSendRequest);

    /**
     * 按照模版编号发送短信
     */
    SmsRespCommand<Map<String, String>> addSmsTemplate(AddSmsTemplateReqCommand addSmsTemplateReqCommand);

    /**
     * 按照模版编号查询模版状态
     */
    SmsRespCommand<Integer> querySmsTemplateStatus(QuerySmsTemplateReqCommand querySmsTemplateReqCommand);




    /**
     * 外部适配器销毁接口
     */
    void destroy();

}

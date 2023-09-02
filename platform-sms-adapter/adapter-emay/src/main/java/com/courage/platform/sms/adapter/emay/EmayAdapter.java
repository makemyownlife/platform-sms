package com.courage.platform.sms.adapter.emay;

import cn.emay.sdk.client.SmsSDKClient;
import cn.emay.sdk.core.dto.sms.common.ResultModel;
import cn.emay.sdk.core.dto.sms.request.SmsSingleRequest;
import cn.emay.sdk.core.dto.sms.response.SmsResponse;
import cn.emay.sdk.util.exception.SDKParamsException;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.SendSmsCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@SPI("emay")
public class EmayAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(EmayAdapter.class);

    private final static String extendedCode = "01";

    private SmsChannelConfig smsChannelConfig;

    private String instanceId = UUID.randomUUID().toString().replaceAll("-", "");

    private SmsSDKClient client;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) throws SDKParamsException {
        logger.info("初始化亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "]");
        this.smsChannelConfig = smsChannelConfig;
        String channelDomain = smsChannelConfig.getChannelDomain();
        String appId = smsChannelConfig.getChannelAppkey();
        String appSecret = smsChannelConfig.getChannelAppsecret();
        this.client = new SmsSDKClient(channelDomain, -1, appId, appSecret);
    }

    @Override
    public SmsResponseCommand sendSmsByTemplateId(SendSmsCommand smsSendRequest) {
        String mobile = smsSendRequest.getPhoneNumbers();
        String signName = smsSendRequest.getSignName();
        String content = "【张勇的博客】短信内容";
        String customSmsId = "1";
        SmsSingleRequest request = new SmsSingleRequest(mobile, content, customSmsId, extendedCode, "");
        ResultModel<SmsResponse> result = client.sendSingleSms(request);
        if ("SUCCESS".equals(result.getCode())) {

        }
        return null;
    }

    @Override
    public SmsResponseCommand addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand) {
        return null;
    }

    @Override
    public void destroy() {
        logger.warn("销毁亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "]");
    }

}

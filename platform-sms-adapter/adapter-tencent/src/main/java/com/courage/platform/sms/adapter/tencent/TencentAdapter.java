package com.courage.platform.sms.adapter.tencent;

import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.request.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.request.QuerySmsTemplateCommand;
import com.courage.platform.sms.adapter.command.request.SendSmsCommand;
import com.courage.platform.sms.adapter.command.response.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@SPI("tencent")
public class TencentAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TencentAdapter.class);

    private SmsChannelConfig smsChannelConfig;

    private SmsClient client;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) {
        logger.info("初始化腾讯短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "]");
        this.smsChannelConfig = smsChannelConfig;
        Credential cred = new Credential(smsChannelConfig.getChannelAppkey(), smsChannelConfig.getChannelAppsecret());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(60);
        httpProfile.setEndpoint("sms.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        this.client = new SmsClient(cred, "ap-guangzhou", clientProfile);
    }

    @Override
    public SmsResponseCommand<String> sendSmsByTemplateId(SendSmsCommand smsSendRequest) {
        return null;
    }

    @Override
    public SmsResponseCommand<Map<String, String>> addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand) {
        return null;
    }

    @Override
    public SmsResponseCommand<Integer> querySmsTemplateStatus(QuerySmsTemplateCommand querySmsTemplateCommand) {
        return null;
    }

    @Override
    public void destroy() {
        logger.warn("销毁腾讯短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "]");
    }

}

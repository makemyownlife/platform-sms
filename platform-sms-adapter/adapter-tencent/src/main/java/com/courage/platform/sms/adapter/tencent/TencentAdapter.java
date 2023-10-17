package com.courage.platform.sms.adapter.tencent;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.request.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.request.QuerySmsTemplateCommand;
import com.courage.platform.sms.adapter.command.request.SendSmsCommand;
import com.courage.platform.sms.adapter.command.response.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.adapter.support.SmsTemplateUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@SPI("tencent")
public class TencentAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(TencentAdapter.class);

    private SmsChannelConfig smsChannelConfig;

    private SmsClient client;

    private String appId;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) {
        logger.info("初始化腾讯短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "]");
        this.smsChannelConfig = smsChannelConfig;
        this.appId = smsChannelConfig.getExtProperties();
        // SecretId、SecretKey 查询: https://console.cloud.tencent.com/cam/capi
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
    public SmsResponseCommand<String> sendSmsByTemplateId(SendSmsCommand sendSmsCommand) {
        // 参考腾讯文档 ：https://cloud.tencent.com/document/product/382/43194
        try {
            SendSmsRequest sendSmsRequest = new SendSmsRequest();
            sendSmsRequest.setSmsSdkAppId(appId);
            sendSmsRequest.setSignName(sendSmsCommand.getSignName());
            sendSmsRequest.setTemplateId(sendSmsCommand.getTemplateCode());
            // 模板参数的个数需要与 TemplateId 对应模板的变量个数保持一致，若无模板参数，则设置为空
            sendSmsRequest.setTemplateParamSet(SmsTemplateUtil.renderTemplateParamArray(sendSmsCommand.getTemplateParam(), sendSmsCommand.getTemplateContent()));
            sendSmsRequest.setPhoneNumberSet(new String[]{
                    "+86" + sendSmsCommand.getPhoneNumbers()
            });
            sendSmsRequest.setExtendCode(StringUtils.EMPTY);
            sendSmsRequest.setSenderId(StringUtils.EMPTY);
            logger.info("tencent send sms:" + JSON.toJSONString(sendSmsRequest));
            SendSmsResponse response = client.SendSms(sendSmsRequest);
            logger.info("response:" + JSON.toJSONString(response));
            SendStatus[] sendStatuArray = response.getSendStatusSet();
            if (sendStatuArray != null && sendStatuArray.length > 0) {
                SendStatus sendStatus = sendStatuArray[0];
                if ("Ok".equals(sendStatus.getCode())) {
                    return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, sendStatus.getSerialNo());
                }
            }
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
        } catch (Exception e) {
            logger.error("tencent sendSms error:", e);
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
        }
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

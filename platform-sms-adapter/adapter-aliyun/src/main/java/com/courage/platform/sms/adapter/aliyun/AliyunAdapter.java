package com.courage.platform.sms.adapter.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.AddSmsTemplateResponse;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.SendSmsCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI("aliyun")
public class AliyunAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(AliyunAdapter.class);

    private SmsChannelConfig smsChannelConfig;

    private Client client;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) throws Exception {
        this.smsChannelConfig = smsChannelConfig;
        logger.info("阿里云短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppKey() + "]");
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(smsChannelConfig.getChannelAppKey())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(smsChannelConfig.getChannelAppSecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        this.client = new com.aliyun.dysmsapi20170525.Client(config);
    }

    @Override
    public SmsResponseCommand sendSmsByTemplateId(SendSmsCommand sendSmsCommand) {
        try {
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest().
                    setSignName(sendSmsCommand.getSignName()).
                    setTemplateCode(sendSmsCommand.getTemplateCode()).
                    setPhoneNumbers(sendSmsCommand.getPhoneNumbers()).
                    setTemplateParam(sendSmsCommand.getTemplateParam());
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            com.aliyun.dysmsapi20170525.models.SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
            if ("ok".equals(resp.getStatusCode())) {
                return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, JSON.toJSONString(resp.getBody()));
            }
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
        } catch (Exception e) {
            logger.error("aliyun sendSms:", e);
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE, e.getMessage());
        }
    }

    @Override
    public SmsResponseCommand addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand) {
        try {
            com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest addSmsTemplateRequest = new com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest();
            addSmsTemplateRequest.setRemark(addSmsTemplateCommand.getRemark());
            addSmsTemplateRequest.setTemplateContent(addSmsTemplateCommand.getTemplateContent());
            addSmsTemplateRequest.setTemplateType(addSmsTemplateCommand.getTemplateType());
            addSmsTemplateRequest.setTemplateName(addSmsTemplateCommand.getTemplateName());
            AddSmsTemplateResponse resp = client.addSmsTemplate(addSmsTemplateRequest);
            if ("ok".equals(resp.getStatusCode())) {
                return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, JSON.toJSONString(resp.getBody()));
            }
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
        } catch (Exception e) {
            logger.error("aliyun addSmsTemplate error :", e);
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE, e.getMessage());
        }
    }

    @Override
    public void destroy() {
        logger.info("销毁阿里云短信客户端渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppKey() + "]");
    }

}

package com.courage.platform.sms.adapter.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.SendSmsRequest;
import com.courage.platform.sms.adapter.command.SmsAdapterResponse;
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
    public SmsAdapterResponse sendSmsByTemplateId(SendSmsRequest smsSendRequest) {
        try {
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest =
                    new com.aliyun.dysmsapi20170525.models.SendSmsRequest().
                            setSignName(smsSendRequest.getSignName()).
                            setTemplateCode(smsSendRequest.getTemplateCode()).
                            setPhoneNumbers(smsSendRequest.getPhoneNumbers()).
                            setTemplateParam(smsSendRequest.getTemplateParam());
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            com.aliyun.dysmsapi20170525.models.SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
            if ("ok".equals(resp.getStatusCode())) {
                return new SmsAdapterResponse(SmsAdapterResponse.SUCCESS_CODE, JSON.toJSONString(resp.getBody()));
            }
            return new SmsAdapterResponse(SmsAdapterResponse.FAIL_CODE);
        } catch (Exception e) {
            logger.error("aliyun sendSms:", e);
            return new SmsAdapterResponse(SmsAdapterResponse.FAIL_CODE, e.getMessage());
        }
    }

    @Override
    public void destroy() {
        logger.info("销毁阿里云短信客户端渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppKey() + "]");
    }

}

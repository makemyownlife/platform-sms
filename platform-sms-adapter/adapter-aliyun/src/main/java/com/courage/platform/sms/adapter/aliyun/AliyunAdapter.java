package com.courage.platform.sms.adapter.aliyun;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.courage.platform.sms.adapter.core.OuterAdapter;
import com.courage.platform.sms.adapter.core.support.SPI;
import com.courage.platform.sms.adapter.core.support.SmsChannelConfig;

@SPI("aliyun")
public class AliyunAdapter implements OuterAdapter {

    private Client client;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(smsChannelConfig.getChannelAppKey())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(smsChannelConfig.getChannelAppSecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        this.client = new com.aliyun.dysmsapi20170525.Client(config);
    }

    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

}

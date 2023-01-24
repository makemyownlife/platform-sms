package com.courage.platform.sms.adapter.alicloud;

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

    public static void main(String[] args) throws Exception {
        // 工程代码泄露可能会导致AccessKey泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.dysmsapi20170525.Client client = createClient("accessKeyId", "accessKeySecret");
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                .setPhoneNumbers("your_value")
                .setTemplateCode("mycode")
                .setSignName("your_value");
        try {
            // 复制代码运行请自行打印 API 的返回值
            SendSmsResponse smsResponse = client.sendSmsWithOptions(sendSmsRequest, new com.aliyun.teautil.models.RuntimeOptions());
        } catch (TeaException error) {
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            // 如有需要，请打印 error
            com.aliyun.teautil.Common.assertAsString(error.message);
        }
    }


}

package cn.javayong.platform.sms.adatper.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.models.AddSmsTemplateResponse;
import com.aliyun.tea.TeaException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by zhangyong on 2023/7/3.
 */
public class AliyunUnitTest {

    private  com.aliyun.dysmsapi20170525.Client client;

    public static com.aliyun.dysmsapi20170525.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new com.aliyun.dysmsapi20170525.Client(config);
    }

    @Before
    public void init() throws Exception {
        client = createClient("", "");
    }

    @Test
    public void sendTemplate() throws Exception {
        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例使用环境变量获取 AccessKey 的方式进行调用，仅供参考，建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html
        com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest().setSignName("阿里云短信测试").setTemplateCode("SMS_154950909").setPhoneNumbers("15011319235").setTemplateParam("{\"code\":\"1234\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            client.sendSmsWithOptions(sendSmsRequest, runtime);
        } catch (TeaException error) {
        } catch (Exception _error) {
        }
    }

    @Test
    public void addSmsTemplate() throws Exception {
        com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest addSmsTemplateRequest = new com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest();
        addSmsTemplateRequest.setTemplateName("测试模版");
        addSmsTemplateRequest.setTemplateType(0);
        addSmsTemplateRequest.setTemplateContent("验证码:${code}");
        addSmsTemplateRequest.setRemark("你好");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            // 复制代码运行请自行打印 API 的返回值
            AddSmsTemplateResponse addSmsTemplateResponse = client.addSmsTemplateWithOptions(addSmsTemplateRequest, runtime);
            System.out.println(JSON.toJSONString(addSmsTemplateResponse));
        } catch (TeaException error) {
            error.printStackTrace();
        } catch (Exception _error) {
            _error.printStackTrace();
        }
    }

}

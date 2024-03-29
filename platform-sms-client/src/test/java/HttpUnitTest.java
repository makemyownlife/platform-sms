import com.alibaba.fastjson.JSON;
import cn.javayong.platform.sms.client.SmsConfig;
import cn.javayong.platform.sms.client.SmsSenderClient;
import cn.javayong.platform.sms.client.SmsSenderResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangyong on 2020/9/12.
 */
public class HttpUnitTest {

    public static void main(String[] args) throws Exception {
        SmsConfig smsConfig = new SmsConfig();
        smsConfig.setAppKey("qQjEiFzn80v8VM4h");
        smsConfig.setSmsServerUrl("http://localhost:8089");
        smsConfig.setAppSecret("9c465ece754bd26a9be77f3d0e2606bd");
        SmsSenderClient smsSenderClient = new SmsSenderClient(smsConfig);
        String mobile = "15011319235";
        String templateId = "555829270636703745";
        // ${code}为您的登录验证码，请于${time}分钟内填写，如非本人操作，请忽略本短信。
        Map<String, String> param = new HashMap<String, String>();
        param.put("code", "1234");
        param.put("time", "10");
        // 1个半小时之后发送
        String attime = String.valueOf(System.currentTimeMillis() + 3600 * 1500L);
        SmsSenderResult senderResult = smsSenderClient.sendSmsByTemplateId(mobile, templateId, attime, param);
        System.out.println("senderResult:" + JSON.toJSONString(senderResult));

    }

}

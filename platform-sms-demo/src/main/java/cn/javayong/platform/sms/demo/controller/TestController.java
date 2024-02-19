package cn.javayong.platform.sms.demo.controller;

import com.alibaba.fastjson.JSON;
import cn.javayong.platform.sms.client.SmsSenderClient;
import cn.javayong.platform.sms.client.SmsSenderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hello")
public class TestController {

    @Autowired
    private SmsSenderClient smsSenderClient;

    @GetMapping("/test")
    public String test() {
        String mobile = "15011319235";
        String templateId = "523419101760679938";
        // 你好，你的信息是：${code}
        Map<String, String> param = new HashMap<String, String>();
        param.put("code", "1234");
        SmsSenderResult senderResult = smsSenderClient.sendSmsByTemplateId(mobile, templateId, param);
        System.out.println("senderResult:" + JSON.toJSONString(senderResult));
        return "hello , first short message !";
    }

}

package com.courage.platform.sms.demo.controller;

import com.courage.platform.sms.client.SmsSenderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        smsSenderClient.sendSmsByTemplateId(mobile, templateId, param);
        return null;
    }

}

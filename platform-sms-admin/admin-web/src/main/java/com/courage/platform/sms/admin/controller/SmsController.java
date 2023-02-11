package com.courage.platform.sms.admin.controller;

import com.courage.platform.sms.admin.model.AdminUser;
import com.courage.platform.sms.admin.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/channels")
    public BaseModel<Map<String, String>> login() {
        Map<String, String> result = new HashMap<>();
        return BaseModel.getInstance(result);
    }

}

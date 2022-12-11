package com.courage.platform.sms.admin.controller;

import com.courage.platform.sms.admin.model.AdminUser;
import com.courage.platform.sms.admin.model.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping(value = "/login")
    public BaseModel<Map<String, String>> login(@RequestBody AdminUser user) {
        Map<String, String> tokenResp = new HashMap<>();
        String token = UUID.randomUUID().toString();
        tokenResp.put("token", token);
        return BaseModel.getInstance(tokenResp);
    }

    @GetMapping(value = "/info")
    public BaseModel info(@RequestParam String token) {
        logger.info("token:" + token);
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername("makemyownlife");
        return BaseModel.getInstance(adminUser);
    }

}

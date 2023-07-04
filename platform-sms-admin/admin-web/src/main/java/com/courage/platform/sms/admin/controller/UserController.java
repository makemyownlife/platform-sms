package com.courage.platform.sms.admin.controller;

import com.courage.platform.sms.admin.controller.model.AdminUser;
import com.courage.platform.sms.admin.controller.model.BaseModel;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    public static final LoadingCache<String, AdminUser> loginUsers = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterAccess(20, TimeUnit.MINUTES)
            .build(key -> null); // 用户登录信息缓存

    @Value("${sms.adminUser}")
    private String adminUser;

    @Value("${sms.adminPasswd}")
    private String adminPasswd;

    @PostMapping(value = "/login")
    public BaseModel<Map<String, String>> login(@RequestBody AdminUser user) {
        Map<String, String> tokenResp = new HashMap<>();
        if (user.getUsername().equals(adminUser) && user.getPassword().equals(adminPasswd)) {
            String token = UUID.randomUUID().toString();
            tokenResp.put("token", token);
            loginUsers.put(token, user);
            return BaseModel.getInstance(tokenResp);
        } else {
            BaseModel<Map<String, String>> model = BaseModel.getInstance(null);
            model.setCode(40001);
            model.setMessage("Invalid username or password");
            return model;
        }
    }

    @GetMapping(value = "/info")
    public BaseModel info(@RequestParam String token) {
        AdminUser user = loginUsers.getIfPresent(token);
        if (user != null) {
            return BaseModel.getInstance(user);
        } else {
            BaseModel<AdminUser> model = BaseModel.getInstance(null);
            model.setCode(50014);
            model.setMessage("Invalid token");
            return model;
        }
    }

}

package com.courage.platform.sms.admin.api.admin;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.admin.common.utils.RedisKeyConstants;
import com.courage.platform.sms.admin.dispatcher.processor.response.ResponseEntity;
import com.courage.platform.sms.admin.domain.vo.AdminUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${sms.adminUser}")
    private String adminUser;

    @Value("${sms.adminPasswd}")
    private String adminPasswd;

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AdminUser user) {
        Map<String, String> tokenResp = new HashMap<>();
        if (user.getUsername().equals(adminUser) && user.getPassword().equals(adminPasswd)) {
            String token = UUID.randomUUID().toString();
            tokenResp.put("token", token);
            redisTemplate.opsForValue().set(RedisKeyConstants.LOGIN_USER + token, JSON.toJSONString(user), 30, TimeUnit.MINUTES);
            return ResponseEntity.success(tokenResp);
        }
        return ResponseEntity.build(40001, "Invalid username or password");
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "X-Token") String token) {
        String userInfoStr = redisTemplate.opsForValue().get(RedisKeyConstants.LOGIN_USER + token);
        if (StringUtils.isNotEmpty(userInfoStr)) {
            redisTemplate.delete(RedisKeyConstants.LOGIN_USER + token);
        }
        return ResponseEntity.success("success");
    }

    @GetMapping(value = "/info")
    public ResponseEntity<String> info(@RequestParam String token) {
        String userInfoStr = redisTemplate.opsForValue().get(RedisKeyConstants.LOGIN_USER + token);
        if (StringUtils.isNotEmpty(userInfoStr)) {
            return ResponseEntity.success(userInfoStr);
        }
        return ResponseEntity.build(50014, "Invalid token");
    }

}

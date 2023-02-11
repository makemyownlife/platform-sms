package com.courage.platform.sms.admin.controller;

import com.courage.platform.sms.admin.model.AdminUser;
import com.courage.platform.sms.admin.model.BaseModel;
import com.courage.platform.sms.admin.service.SmsChannelService;
import com.courage.platform.sms.domain.TSmsChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SmsChannelService smsChannelService;

    @PostMapping(value = "/channels")
    public BaseModel<List<TSmsChannel>> login() {
        return BaseModel.getInstance(smsChannelService.queryChannels());
    }

}
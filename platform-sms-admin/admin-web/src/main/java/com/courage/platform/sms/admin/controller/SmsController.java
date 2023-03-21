package com.courage.platform.sms.admin.controller;

import com.courage.platform.sms.admin.model.AdminUser;
import com.courage.platform.sms.admin.model.BaseModel;
import com.courage.platform.sms.admin.model.Pager;
import com.courage.platform.sms.admin.service.SmsChannelService;
import com.courage.platform.sms.domain.TSmsChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SmsChannelService smsChannelService;

    @PostMapping(value = "/channels")
    public BaseModel<Pager> channels(String channelAppkey, String channelType) {
        Map<String, String> param = new HashMap<>();
        param.put("channelType", channelType);
        param.put("channelAppkey", channelAppkey);
        List<TSmsChannel> tSmsChannelList = smsChannelService.queryChannels(param);
        Pager pager = new Pager();
        pager.setCount(Long.valueOf(tSmsChannelList.size()));
        pager.setItems(tSmsChannelList);
        return BaseModel.getInstance(pager);
    }

    @PostMapping(value = "/addSmsChannel")
    public BaseModel addSmsChannel(String channelAppkey,
                                   String channelType,
                                   String channelAppsecret,
                                   String channelDomain,
                                   String extProperties) {
        TSmsChannel tSmsChannel = new TSmsChannel();
        tSmsChannel.setChannelAppkey(channelAppkey);
        tSmsChannel.setChannelType(channelType);
        tSmsChannel.setChannelAppsecret(channelAppsecret);
        tSmsChannel.setCreateTime(new Date());
        tSmsChannel.setUpdateTime(new Date());
        tSmsChannel.setExtProperties(StringUtils.trimToEmpty(extProperties));
        smsChannelService.addSmsChannel(tSmsChannel);
        return BaseModel.getInstance("success");
    }

}

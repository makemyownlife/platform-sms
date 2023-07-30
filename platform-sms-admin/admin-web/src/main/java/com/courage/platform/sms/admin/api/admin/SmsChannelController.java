package com.courage.platform.sms.admin.api.admin;

import com.courage.platform.sms.admin.vo.BaseModel;
import com.courage.platform.sms.admin.vo.Pager;
import com.courage.platform.sms.admin.loader.service.SmsChannelService;
import com.courage.platform.sms.admin.domain.TSmsChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsChannelController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SmsChannelService smsChannelService;

    @PostMapping(value = "/channels")
    public BaseModel<Pager> channels(String channelAppkey, String channelType) {
        Map<String, String> param = new HashMap<>();
        param.put("channelType", StringUtils.trimToEmpty(channelType));
        param.put("channelAppkey", StringUtils.trimToEmpty(channelAppkey));
        List<TSmsChannel> tSmsChannelList = smsChannelService.queryChannels(param);
        Pager pager = new Pager();
        pager.setCount(Long.valueOf(tSmsChannelList.size()));
        pager.setItems(tSmsChannelList);
        return BaseModel.getInstance(pager);
    }

    @PostMapping(value = "/addSmsChannel")
    public BaseModel addSmsChannel(@RequestBody TSmsChannel tSmsChannel) {
        return smsChannelService.addSmsChannel(tSmsChannel);
    }

    @PostMapping(value = "/updateSmsChannel")
    public BaseModel updateSmsChannel(@RequestBody TSmsChannel tSmsChannel) {
        return smsChannelService.updateSmsChannel(tSmsChannel);
    }

    @PostMapping(value = "/deleteSmsChannel")
    public BaseModel deleteSmsChannel(String id) {
        return smsChannelService.deleteSmsChannel(id);
    }

}

package com.courage.platform.sms.admin.api.admin;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.admin.common.utils.ChannelDictEnum;
import com.courage.platform.sms.admin.common.utils.ResponseEntity;
import com.courage.platform.sms.admin.domain.vo.BaseModel;
import com.courage.platform.sms.admin.domain.vo.Pager;
import com.courage.platform.sms.admin.service.SmsChannelService;
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
    public ResponseEntity<Pager> channels(String channelAppkey, String channelType) {
        Map<String, String> param = new HashMap<>();
        param.put("channelType", StringUtils.trimToEmpty(channelType));
        param.put("channelAppkey", StringUtils.trimToEmpty(channelAppkey));
        List<TSmsChannel> tSmsChannelList = smsChannelService.queryChannels(param);
        Pager pager = new Pager();
        pager.setCount(Long.valueOf(tSmsChannelList.size()));
        pager.setItems(tSmsChannelList);
        return ResponseEntity.success(pager);
    }

    @PostMapping(value = "/channelDict")
    public ResponseEntity channelDict() {
        List<Map<String, String>> channelDicList = new ArrayList<>();
        for (ChannelDictEnum channel : ChannelDictEnum.values()) {
            Map<String, String> channelMap = new HashMap<>();
            channelMap.put("channelType", channel.getChannelType());
            channelMap.put("channelName", channel.getChannelName());
            channelMap.put("domain", channel.getDomain());
            channelDicList.add(channelMap);
        }
        return ResponseEntity.success(channelDicList);
    }

    @PostMapping(value = "/addSmsChannel")
    public ResponseEntity<String> addSmsChannel(@RequestBody TSmsChannel tSmsChannel) {
        return smsChannelService.addSmsChannel(tSmsChannel);
    }

    @PostMapping(value = "/updateSmsChannel")
    public ResponseEntity updateSmsChannel(@RequestBody TSmsChannel tSmsChannel) {
        return smsChannelService.updateSmsChannel(tSmsChannel);
    }

    @PostMapping(value = "/deleteSmsChannel")
    public ResponseEntity<String> deleteSmsChannel(String id) {
        return smsChannelService.deleteSmsChannel(id);
    }

}

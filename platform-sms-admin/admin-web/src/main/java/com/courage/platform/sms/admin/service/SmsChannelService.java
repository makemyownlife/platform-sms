package com.courage.platform.sms.admin.service;

import com.courage.platform.sms.admin.common.utils.ResponseEntity;
import com.courage.platform.sms.admin.domain.TSmsChannel;

import java.util.List;
import java.util.Map;

public interface SmsChannelService {

    List<TSmsChannel> queryChannels(Map<String, String> param);

    ResponseEntity<String> addSmsChannel(TSmsChannel tSmsChannel);

    ResponseEntity updateSmsChannel(TSmsChannel tSmsChannel);

    ResponseEntity<String> deleteSmsChannel(String id);

}

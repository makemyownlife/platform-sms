package com.courage.platform.sms.admin.service;

import com.courage.platform.sms.domain.TSmsChannel;

import java.util.List;
import java.util.Map;

public interface SmsChannelService {

    List<TSmsChannel> queryChannels(Map<String, String> param);

}

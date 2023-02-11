package com.courage.platform.sms.admin.service;

import com.courage.platform.sms.domain.TSmsChannel;

import java.util.List;

public interface SmsChannelService {

    List<TSmsChannel> queryChannels();

}

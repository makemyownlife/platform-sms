package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.service.SmsChannelService;
import com.courage.platform.sms.dao.TSmsChannelDAO;
import com.courage.platform.sms.domain.TSmsChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SmsChannelServiceImpl implements SmsChannelService {

    @Autowired
    private TSmsChannelDAO tSmsChannelDAO;

    public List<TSmsChannel> queryChannels(Map<String, String> param) {
        return tSmsChannelDAO.queryChannels(param);
    }

}

package com.courage.platform.sms.worker.config;

import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.dao.TSmsChannelDAO;
import com.courage.platform.sms.domain.TSmsChannel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangyong on 2023/2/10.
 */
@Component
public class SmsAdapterConfig {

    @Autowired
    private TSmsChannelDAO tSmsChannelDAO;

    public List<SmsChannelConfig> getChannelConfigs() {
        List<SmsChannelConfig> channelConfigs = new ArrayList<SmsChannelConfig>();
        List<TSmsChannel> tSmsChannels = tSmsChannelDAO.queryChannels();
        for (TSmsChannel tSmsChannel : tSmsChannels) {
            SmsChannelConfig smsChannelConfig = new SmsChannelConfig();
            smsChannelConfig.setId(Long.valueOf(tSmsChannel.getId()));
            smsChannelConfig.setChannelType(tSmsChannel.getChannelType());
            smsChannelConfig.setChannelAppKey(tSmsChannel.getChannelAppkey());
            smsChannelConfig.setChannelAppSecret(tSmsChannel.getChannelAppsecret());
            smsChannelConfig.setChannelDomain(tSmsChannel.getChannelDomain());
            smsChannelConfig.setExtProperties(StringUtils.trimToEmpty(tSmsChannel.getExtProperties()));
            channelConfigs.add(smsChannelConfig);
        }
        return channelConfigs;
    }

}

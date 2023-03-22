package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.model.BaseModel;
import com.courage.platform.sms.admin.service.SmsChannelService;
import com.courage.platform.sms.dao.TSmsChannelDAO;
import com.courage.platform.sms.domain.TSmsChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SmsChannelServiceImpl implements SmsChannelService {

    private final static Logger logger = LoggerFactory.getLogger(SmsChannelServiceImpl.class);

    @Autowired
    private TSmsChannelDAO tSmsChannelDAO;

    public List<TSmsChannel> queryChannels(Map<String, String> param) {
        return tSmsChannelDAO.queryChannels(param);
    }

    @Override
    public BaseModel addSmsChannel(TSmsChannel tSmsChannel) {
        try {
            TSmsChannel item = tSmsChannelDAO.queryChannelByAppkeyAndChannelType(tSmsChannel.getChannelAppkey(), tSmsChannel.getChannelType());
            tSmsChannel.setCreateTime(new Date());
            tSmsChannel.setUpdateTime(new Date());
            tSmsChannel.setExtProperties(StringUtils.trimToEmpty(tSmsChannel.getExtProperties()));
            tSmsChannel.setStatus((byte) 0);
            tSmsChannel.setSendOrder(0);
            if (item == null) {
                tSmsChannelDAO.insert(tSmsChannel);
            } else {
                tSmsChannelDAO.updateByPrimaryKey(tSmsChannel);
            }
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("addSmsChannel error:", e);
            return BaseModel.getInstance("fail");
        }
    }

}

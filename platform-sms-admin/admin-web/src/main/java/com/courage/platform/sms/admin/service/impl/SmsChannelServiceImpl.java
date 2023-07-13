package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.controller.model.BaseModel;
import com.courage.platform.sms.admin.dao.TSmsChannelDAO;
import com.courage.platform.sms.admin.domain.TSmsChannel;
import com.courage.platform.sms.admin.service.SmsChannelService;
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
            tSmsChannel.setStatus(0);
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

    @Override
    public BaseModel updateSmsChannel(TSmsChannel tSmsChannel) {
        try {
            tSmsChannel.setUpdateTime(new Date());
            tSmsChannel.setExtProperties(StringUtils.trimToEmpty(tSmsChannel.getExtProperties()));
            tSmsChannel.setStatus(0);
            tSmsChannelDAO.updateByPrimaryKey(tSmsChannel);
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("addSmsChannel error:", e);
            return BaseModel.getInstance("fail");
        }
    }

    @Override
    public BaseModel deleteSmsChannel(String id) {
        try {
            tSmsChannelDAO.deleteByPrimaryKey(Integer.valueOf(id));
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("deleteSmsChannel error:", e);
            return BaseModel.getInstance("fail");
        }
    }

}

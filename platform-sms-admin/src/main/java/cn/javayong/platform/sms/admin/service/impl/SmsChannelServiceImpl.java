package cn.javayong.platform.sms.admin.service.impl;

import cn.javayong.platform.sms.admin.common.utils.ResponseEntity;
import cn.javayong.platform.sms.admin.dao.TSmsChannelDAO;
import cn.javayong.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import cn.javayong.platform.sms.admin.domain.TSmsChannel;
import cn.javayong.platform.sms.admin.service.SmsChannelService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SmsChannelServiceImpl implements SmsChannelService {

    private final static Logger logger = LoggerFactory.getLogger(SmsChannelServiceImpl.class);

    @Autowired
    private TSmsChannelDAO tSmsChannelDAO;

    @Autowired
    private TSmsTemplateBindingDAO tSmsTemplateBindingDAO;

    public List<TSmsChannel> queryChannels(Map<String, String> param) {
        return tSmsChannelDAO.queryChannels(param);
    }

    @Override
    public ResponseEntity<String> addSmsChannel(TSmsChannel tSmsChannel) {
        try {
            TSmsChannel item = tSmsChannelDAO.queryChannelByAppkeyAndChannelType(tSmsChannel.getChannelAppkey(), tSmsChannel.getChannelType());
            tSmsChannel.setCreateTime(new Date());
            tSmsChannel.setUpdateTime(new Date());
            tSmsChannel.setExtProperties(StringUtils.trimToEmpty(tSmsChannel.getExtProperties()));
            tSmsChannel.setStatus(0);
            tSmsChannel.setSendOrder(0);
            String md5Value = DigestUtils.md5DigestAsHex((
                                                                         tSmsChannel.getId() +
                                                                         tSmsChannel.getChannelAppkey() +
                                                                         tSmsChannel.getChannelAppsecret() +
                                                                         tSmsChannel.getChannelDomain() +
                                                                         tSmsChannel.getChannelName() +
                                                                         tSmsChannel.getChannelType() +
                                                                         tSmsChannel.getExtProperties()).getBytes("UTF-8"));
            tSmsChannel.setMd5Value(md5Value);
            if (item == null) {
                tSmsChannelDAO.insert(tSmsChannel);
            } else {
                tSmsChannelDAO.updateByPrimaryKey(tSmsChannel);
            }
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("addSmsChannel error:", e);
            return ResponseEntity.fail("fail");
        }
    }

    @Override
    public ResponseEntity updateSmsChannel(TSmsChannel tSmsChannel) {
        try {
            tSmsChannel.setUpdateTime(new Date());
            tSmsChannel.setExtProperties(StringUtils.trimToEmpty(tSmsChannel.getExtProperties()));
            tSmsChannel.setStatus(0);
            String md5Value = DigestUtils.md5DigestAsHex((tSmsChannel.getId() + tSmsChannel.getChannelAppkey() + tSmsChannel.getChannelAppsecret() + tSmsChannel.getChannelDomain() + tSmsChannel.getChannelName() + tSmsChannel.getChannelType() + tSmsChannel.getExtProperties()).getBytes("UTF-8"));
            tSmsChannel.setMd5Value(md5Value);
            tSmsChannelDAO.updateByPrimaryKey(tSmsChannel);
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("addSmsChannel error:", e);
            return ResponseEntity.fail("修改渠道失败!");
        }
    }

    @Override
    public ResponseEntity<String> deleteSmsChannel(String id) {
        try {
            //判断是否有已经绑定的模版
            Integer count = tSmsTemplateBindingDAO.selectCountByChannelId(Integer.valueOf(id));
            if (count > 0) {
                return ResponseEntity.fail("该渠道已经创建了" + count + "个模版，无法删除");
            }
            tSmsChannelDAO.deleteByPrimaryKey(Integer.valueOf(id));
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("deleteSmsChannel error:", e);
            return ResponseEntity.fail("fail");
        }
    }

}

package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.common.config.RedisConfig;
import com.courage.platform.sms.admin.common.utils.ResponseEntity;
import com.courage.platform.sms.admin.dao.TSmsAppinfoDAO;
import com.courage.platform.sms.admin.domain.TSmsAppinfo;
import com.courage.platform.sms.admin.service.AppInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AppInfoServiceImpl implements AppInfoService {

    private final static Logger logger = LoggerFactory.getLogger(SmsChannelServiceImpl.class);

    @Autowired
    private TSmsAppinfoDAO tSmsAppinfoDAO;

    @Autowired
    private RedisTemplate redisTemplate;

    private final static ConcurrentHashMap<String, TSmsAppinfo> localAppCache = new ConcurrentHashMap<String, TSmsAppinfo>(1024);

    @PostConstruct
    @Scheduled(cron = "0/30 * * * * ?")
    public void startLoadAppInfo() {
        long start = System.currentTimeMillis();
        logger.info("开始加载应用列表到缓存中");
        try {
            List<TSmsAppinfo> appinfoList = tSmsAppinfoDAO.selectAll();
            appinfoList.forEach(item -> {
                localAppCache.put(item.getAppKey(), item);
            });
        } catch (Exception e) {
            logger.error("startLoadAppInfo error: ", e);
        } finally {
            logger.info("结束加载应用列表到缓存中，总耗时:" + (System.currentTimeMillis() - start) + "ms");
        }
    }

    @Override
    public List<TSmsAppinfo> selectAppInfoListPage(Map<String, Object> param) {
        return tSmsAppinfoDAO.selectAppInfoListPage(param);
    }

    @Override
    public TSmsAppinfo getAppinfoByAppKeyFromLocalCache(String appKey) {
        return localAppCache.get(appKey);
    }

    @Override
    public void reloadAppInfoLocalCache(String appKey) {
        logger.info("重新加载应用key:" + appKey);
        TSmsAppinfo tSmsAppinfo = tSmsAppinfoDAO.getAppinfoByAppKey(appKey);
        if (tSmsAppinfo == null) {
            localAppCache.remove(appKey);
        } else {
            localAppCache.put(appKey, tSmsAppinfo);
        }
    }

    @Override
    public TSmsAppinfo getAppInfoByAppkey(String appkey) {
        return tSmsAppinfoDAO.getAppinfoByAppKey(appkey);
    }

    @Override
    public Integer selectAppInfoCount(Map<String, Object> param) {
        return tSmsAppinfoDAO.selectAppInfoCount(param);
    }

    @Override
    public ResponseEntity addAppInfo(TSmsAppinfo tSmsAppinfo) {
        try {
            TSmsAppinfo item = tSmsAppinfoDAO.getAppinfoByAppKey(tSmsAppinfo.getAppKey());
            tSmsAppinfo.setCreateTime(new Date());
            tSmsAppinfo.setUpdateTime(new Date());
            tSmsAppinfo.setStatus((byte) 0);
            if (item == null) {
                tSmsAppinfoDAO.insert(tSmsAppinfo);
                sendMessageToChannel(tSmsAppinfo.getAppKey());
                return ResponseEntity.success("success");
            } else {
                return ResponseEntity.fail("应用已经存在，请仔细核对!");
            }
        } catch (Exception e) {
            logger.error("addAppInfo error:", e);
            return ResponseEntity.fail("添加应用失败");
        }
    }

    @Override
    public ResponseEntity updateAppInfo(TSmsAppinfo tSmsAppinfo) {
        try {
            TSmsAppinfo item = tSmsAppinfoDAO.selectByPrimaryKey(tSmsAppinfo.getId());
            if (item != null) {
                tSmsAppinfo.setId(tSmsAppinfo.getId());
                tSmsAppinfo.setUpdateTime(new Date());
                tSmsAppinfo.setStatus((byte) 0);
                tSmsAppinfoDAO.updateByPrimaryKey(tSmsAppinfo);
                sendMessageToChannel(item.getAppKey());
            }
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("updateAppInfo error:", e);
            return ResponseEntity.fail("添加应用失败");
        }
    }

    @Override
    public ResponseEntity deleteAppInfo(String id) {
        try {
            TSmsAppinfo item = tSmsAppinfoDAO.selectByPrimaryKey(Long.valueOf(id));
            if (item != null) {
                tSmsAppinfoDAO.deleteByPrimaryKey(Long.valueOf(id));
                sendMessageToChannel(item.getAppKey());
            }
            return ResponseEntity.success("success");
        } catch (Exception e) {
            logger.error("deleteSmsChannel error:", e);
            return ResponseEntity.fail("删除应用失败");
        }
    }

    private void sendMessageToChannel(String appKey) {
        redisTemplate.convertAndSend(RedisConfig.APPINFO_CHANGE_CHANNEL_TOPIC, appKey);
    }

}

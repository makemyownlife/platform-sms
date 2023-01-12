package com.courage.platform.sms.server.service;

import com.courage.platform.sms.dao.TSmsAppinfoDAO;
import com.courage.platform.sms.domain.TSmsAppinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangyong on 2023/1/5.
 */
@Service
public class AppInfoService {

    private static final Logger logger = LoggerFactory.getLogger(AppInfoService.class);

    private final static ConcurrentHashMap<String, TSmsAppinfo> localAppCache = new ConcurrentHashMap<String, TSmsAppinfo>(1024);

    @Autowired
    private TSmsAppinfoDAO tSmsAppinfoDAO;

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

    public TSmsAppinfo getAppinfoByAppKeyFromLocalCache(String appKey) {
        return localAppCache.get(appKey);
    }

}

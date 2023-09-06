package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.dao.TSmsAppinfoDAO;
import com.courage.platform.sms.admin.domain.TSmsAppinfo;
import com.courage.platform.sms.admin.service.AppInfoService;
import com.courage.platform.sms.admin.domain.vo.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public TSmsAppinfo getAppInfoByAppkey(String appkey) {
        return tSmsAppinfoDAO.getAppinfoByAppKey(appkey);
    }

    @Override
    public Integer selectAppInfoCount(Map<String, Object> param) {
        return tSmsAppinfoDAO.selectAppInfoCount(param);
    }

    @Override
    public BaseModel addAppInfo(TSmsAppinfo tSmsAppinfo) {
        try {
            TSmsAppinfo item = tSmsAppinfoDAO.getAppinfoByAppKey(tSmsAppinfo.getAppKey());
            tSmsAppinfo.setCreateTime(new Date());
            tSmsAppinfo.setUpdateTime(new Date());
            tSmsAppinfo.setStatus((byte) 0);
            if (item == null) {
                tSmsAppinfoDAO.insert(tSmsAppinfo);
                return BaseModel.getInstance("success");
            } else {
                return BaseModel.getInstance("fail", "应用已经存在，请仔细核对!");
            }
        } catch (Exception e) {
            logger.error("addAppInfo error:", e);
            return BaseModel.getInstance("fail");
        }
    }

    @Override
    public BaseModel updateAppInfo(TSmsAppinfo tSmsAppinfo) {
        try {
            TSmsAppinfo item = tSmsAppinfoDAO.selectByPrimaryKey(tSmsAppinfo.getId());
            if (item != null) {
                tSmsAppinfo.setId(tSmsAppinfo.getId());
                tSmsAppinfo.setUpdateTime(new Date());
                tSmsAppinfo.setStatus((byte) 0);
                tSmsAppinfoDAO.updateByPrimaryKey(tSmsAppinfo);
            }
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("updateAppInfo error:", e);
            return BaseModel.getInstance("fail");
        }
    }

    @Override
    public BaseModel deleteAppInfo(String id) {
        try {
            tSmsAppinfoDAO.deleteByPrimaryKey(Integer.valueOf(id));
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("deleteSmsChannel error:", e);
            return BaseModel.getInstance("fail");
        }
    }

}

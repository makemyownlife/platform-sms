package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.dao.TSmsAppinfoDAO;
import com.courage.platform.sms.admin.domain.TSmsAppinfo;
import com.courage.platform.sms.admin.model.BaseModel;
import com.courage.platform.sms.admin.service.AppInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AppInfoServiceImpl implements AppInfoService {

    private final static Logger logger = LoggerFactory.getLogger(SmsChannelServiceImpl.class);

    @Autowired
    private TSmsAppinfoDAO tSmsAppinfoDAO;

    @Override
    public List<TSmsAppinfo> selectAppInfoListPage(Map<String, Object> param) {
        return tSmsAppinfoDAO.selectAppInfoListPage(param);
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
            tSmsAppinfoDAO.deleteByPrimaryKey(Long.valueOf(id));
            return BaseModel.getInstance("success");
        } catch (Exception e) {
            logger.error("deleteSmsChannel error:", e);
            return BaseModel.getInstance("fail");
        }
    }

}

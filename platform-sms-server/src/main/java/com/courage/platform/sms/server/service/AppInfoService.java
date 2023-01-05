package com.courage.platform.sms.server.service;

import com.courage.platform.sms.dao.TSmsAppinfoDAO;
import com.courage.platform.sms.domain.TSmsAppinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangyong on 2023/1/5.
 */
@Service
public class AppInfoService {

    private static final Logger logger = LoggerFactory.getLogger(AppInfoService.class);

    @Autowired
    private TSmsAppinfoDAO tSmsAppinfoDAO;

    public TSmsAppinfo getAppinfoByAppKey(String appKey) {
        return tSmsAppinfoDAO.getAppinfoByAppKey(appKey);
    }

}

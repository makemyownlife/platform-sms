package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.service.AppInfoService;
import com.courage.platform.sms.dao.TSmsAppinfoDAO;
import com.courage.platform.sms.domain.TSmsAppinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AppInfoServiceImpl implements AppInfoService {

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

}

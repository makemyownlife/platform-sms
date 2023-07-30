package com.courage.platform.sms.admin.loader.service;

import com.courage.platform.sms.admin.vo.BaseModel;
import com.courage.platform.sms.admin.domain.TSmsAppinfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AppInfoService {

    List<TSmsAppinfo> selectAppInfoListPage(Map<String, Object> param);

    Integer selectAppInfoCount(Map<String, Object> param);

    BaseModel addAppInfo(TSmsAppinfo tSmsAppinfo);

    BaseModel updateAppInfo(TSmsAppinfo tSmsAppinfo);

    BaseModel deleteAppInfo(String id);

}

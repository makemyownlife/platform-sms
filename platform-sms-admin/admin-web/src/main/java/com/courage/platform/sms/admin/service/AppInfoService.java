package com.courage.platform.sms.admin.service;

import com.courage.platform.sms.admin.dispatcher.processor.response.ResponseEntity;
import com.courage.platform.sms.admin.domain.TSmsAppinfo;
import com.courage.platform.sms.admin.domain.vo.BaseModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AppInfoService {

    List<TSmsAppinfo> selectAppInfoListPage(Map<String, Object> param);

    TSmsAppinfo getAppinfoByAppKeyFromLocalCache(String appKey);

    Integer selectAppInfoCount(Map<String, Object> param);

    ResponseEntity addAppInfo(TSmsAppinfo tSmsAppinfo);

    ResponseEntity updateAppInfo(TSmsAppinfo tSmsAppinfo);

    ResponseEntity deleteAppInfo(String id);

    TSmsAppinfo getAppInfoByAppkey(String appkey);

}

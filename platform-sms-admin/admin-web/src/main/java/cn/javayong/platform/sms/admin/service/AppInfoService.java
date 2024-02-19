package cn.javayong.platform.sms.admin.service;

import cn.javayong.platform.sms.admin.common.utils.ResponseEntity;
import cn.javayong.platform.sms.admin.domain.TSmsAppinfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AppInfoService {

    List<TSmsAppinfo> selectAppInfoListPage(Map<String, Object> param);

    TSmsAppinfo getAppinfoByAppKeyFromLocalCache(String appKey);

    void reloadAppInfoLocalCache(String appKey);

    Integer selectAppInfoCount(Map<String, Object> param);

    ResponseEntity addAppInfo(TSmsAppinfo tSmsAppinfo);

    ResponseEntity updateAppInfo(TSmsAppinfo tSmsAppinfo);

    ResponseEntity deleteAppInfo(String id);

    TSmsAppinfo getAppInfoByAppkey(String appkey);

}

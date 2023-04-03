package com.courage.platform.sms.admin.service;

import com.courage.platform.sms.domain.TSmsAppinfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface AppInfoService {

    List<TSmsAppinfo> selectAppInfoListPage(Map<String, Object> param);

    Integer selectAppInfoCount(Map<String, Object> param);

}

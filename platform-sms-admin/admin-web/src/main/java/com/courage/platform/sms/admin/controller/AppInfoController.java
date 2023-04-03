package com.courage.platform.sms.admin.controller;

import com.courage.platform.sms.admin.model.BaseModel;
import com.courage.platform.sms.admin.model.Pager;
import com.courage.platform.sms.admin.service.AppInfoService;
import com.courage.platform.sms.admin.service.SmsChannelService;
import com.courage.platform.sms.domain.TSmsAppinfo;
import com.courage.platform.sms.domain.TSmsChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sms")
public class AppInfoController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AppInfoService appInfoService;

    @PostMapping(value = "/appList")
    public BaseModel<Pager> appList(String appkey, int page, int size) {
        Map<String, Object> param = new HashMap<>();
        param.put("appkey", appkey);
        param.put("page", page);
        param.put("size", size);
        List<TSmsAppinfo> appinfoList = appInfoService.selectAppInfoListPage(param);
        Integer count = appInfoService.selectAppInfoCount(param);
        Pager pager = new Pager();
        pager.setCount(Long.valueOf(count));
        pager.setItems(appinfoList);
        return BaseModel.getInstance(pager);
    }

    @PostMapping(value = "/addAppInfo")
    public BaseModel addAppInfo(@RequestBody TSmsAppinfo appinfo) {
        return BaseModel.getInstance(null);
    }

    @PostMapping(value = "/updateAppInfo")
    public BaseModel updateAppInfo(@RequestBody TSmsAppinfo appinfo) {
        return BaseModel.getInstance(null);
    }

    @PostMapping(value = "/deleteAppInfo")
    public BaseModel deleteAppInfo(String id) {
        return BaseModel.getInstance(null);
    }


}

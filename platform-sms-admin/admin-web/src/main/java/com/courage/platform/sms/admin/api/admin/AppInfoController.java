package com.courage.platform.sms.admin.api.admin;

import com.courage.platform.sms.admin.domain.TSmsAppinfo;
import com.courage.platform.sms.admin.domain.vo.BaseModel;
import com.courage.platform.sms.admin.domain.vo.Pager;
import com.courage.platform.sms.admin.service.AppInfoService;
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
        return appInfoService.addAppInfo(appinfo);
    }

    @PostMapping(value = "/updateAppInfo")
    public BaseModel updateAppInfo(@RequestBody TSmsAppinfo appinfo) {
        return appInfoService.updateAppInfo(appinfo);
    }

    @PostMapping(value = "/deleteAppInfo")
    public BaseModel deleteAppInfo(String id) {
        if (Integer.valueOf(id) == 1) {
            logger.info("默认应用不能删除");
            return BaseModel.getInstance("error");
        }
        return appInfoService.deleteAppInfo(id);
    }


}

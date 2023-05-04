package com.courage.platform.sms.admin.controller;

import com.courage.platform.sms.admin.model.BaseModel;
import com.courage.platform.sms.admin.model.Pager;
import com.courage.platform.sms.domain.TSmsChannel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模版控制器
 * Created by zhangyong on 2023/5/4.
 */
@RestController
@RequestMapping("/api/v1/sms")
public class TemplateController {

    @PostMapping(value = "/templates")
    public BaseModel<Pager> channels(String templateName, String channelType) {
        Map<String, String> param = new HashMap<>();
        param.put("templateName", templateName);
        Pager pager = new Pager();
//        pager.setCount();
//        pager.setItems();
        return BaseModel.getInstance(pager);
    }

}

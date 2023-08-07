package com.courage.platform.sms.admin.api.admin;

import com.courage.platform.sms.admin.dao.domain.TSmsRecordDetail;
import com.courage.platform.sms.admin.service.SmsRecordService;
import com.courage.platform.sms.admin.vo.BaseModel;
import com.courage.platform.sms.admin.vo.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/sms")
public class SmsRecordController {

    private final static Logger logger = LoggerFactory.getLogger(SmsRecordController.class);

    @Autowired
    private SmsRecordService smsRecordService;

    @PostMapping(value = "/records")
    public BaseModel<Pager> records(String mobile, String startTime, String endTime, String page, String size) {
        Map<String, Object> param = new HashMap<>();
        param.put("mobile", mobile);
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        param.put("page", Integer.valueOf(page));
        param.put("size", Integer.valueOf(size));

        Pager pager = new Pager();
        List<TSmsRecordDetail> detailList = smsRecordService.queryRecordDetailList(param);
        Long count = smsRecordService.queryCountRecordDetail(param);
        pager.setItems(detailList);
        pager.setCount(count);
        return BaseModel.getInstance(pager);
    }

    @PostMapping(value = "/addSmsRecord")
    public BaseModel<String> addSmsRecord(String mobile, String templateId) {
        return smsRecordService.adminSendRecord(mobile, templateId);
    }

}

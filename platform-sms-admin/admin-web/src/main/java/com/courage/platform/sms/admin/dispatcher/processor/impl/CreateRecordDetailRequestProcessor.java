package com.courage.platform.sms.admin.dispatcher.processor.impl;

import com.courage.platform.sms.admin.dispatcher.processor.SmsAdatperProcessor;
import com.courage.platform.sms.admin.dispatcher.processor.RequestCommand;
import com.courage.platform.sms.admin.dispatcher.processor.ResponseCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建记录详情
 * Created by zhangyong on 2023/8/20.
 */
@Component
public class CreateRecordDetailRequestProcessor implements SmsAdatperProcessor<Long, List<Long>> {

    private static Logger logger = LoggerFactory.getLogger(CreateRecordDetailRequestProcessor.class);

    @Override
    public ResponseCommand<List<Long>> processRequest(RequestCommand<Long> recordId) {
        List<Long> detailList = new ArrayList<>();
        return ResponseCommand.success(detailList);
    }

}

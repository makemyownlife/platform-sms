package com.courage.platform.sms.admin.dispatcher.processor.impl;

import com.courage.platform.sms.admin.common.config.IdGenerator;
import com.courage.platform.sms.admin.dao.TSmsRecordDAO;
import com.courage.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import com.courage.platform.sms.admin.dao.TSmsTemplateDAO;
import com.courage.platform.sms.admin.dao.domain.TSmsRecord;
import com.courage.platform.sms.admin.dao.domain.TSmsTemplate;
import com.courage.platform.sms.admin.dispatcher.SmsAdapterLoader;
import com.courage.platform.sms.admin.dispatcher.SmsAdapterSchedule;
import com.courage.platform.sms.admin.dispatcher.SmsAdatperProcessor;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorRequest;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorResponse;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorResponseCode;
import com.courage.platform.sms.admin.dispatcher.processor.body.SendMessageRequestBody;
import com.courage.platform.sms.client.SmsSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建记录详情
 * Created by zhangyong on 2023/8/20.
 */
@Component
public class CreateRecordDetailRequestProcessor implements SmsAdatperProcessor<Long, List<Long>> {

    private static Logger logger = LoggerFactory.getLogger(CreateRecordDetailRequestProcessor.class);

    @Override
    public ProcessorResponse<List<Long>> processRequest(ProcessorRequest<Long> recordId) {
        List<Long> detailList = new ArrayList<>();
        return ProcessorResponse.success(detailList);
    }

}

package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.dao.TSmsRecordDAO;
import com.courage.platform.sms.admin.dao.TSmsRecordDetailDAO;
import com.courage.platform.sms.admin.dao.domain.TSmsRecordDetail;
import com.courage.platform.sms.admin.dispatcher.SmsAdapterDispatcher;
import com.courage.platform.sms.admin.dispatcher.processor.RequestCommand;
import com.courage.platform.sms.admin.dispatcher.processor.RequestCode;
import com.courage.platform.sms.admin.dispatcher.processor.body.SendMessageRequestBody;
import com.courage.platform.sms.admin.service.SmsRecordService;
import com.courage.platform.sms.admin.vo.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SmsRecordServiceImpl implements SmsRecordService {

    private final Logger logger = LoggerFactory.getLogger(SmsRecordServiceImpl.class);

    @Autowired
    private TSmsRecordDAO recordDAO;

    @Autowired
    private TSmsRecordDetailDAO detailDAO;

    @Autowired
    private SmsAdapterDispatcher smsAdapterDispatcher;

    @Override
    public List<TSmsRecordDetail> queryRecordDetailList(Map<String, Object> param) {
        return detailDAO.queryRecordDetailList(param);
    }

    @Override
    public Long queryCountRecordDetail(Map<String, Object> param) {
        return detailDAO.queryCountRecordDetail(param);
    }

    @Override
    public BaseModel<String> adminSendRecord(String mobile, String templateId, String templateParam) {
        logger.info("admin端发送短信，mobile：" + mobile + " templateId:" + templateId);
        SendMessageRequestBody sendMessageRequestBody = new SendMessageRequestBody();
        sendMessageRequestBody.setAppId("1");                                        // 使用默认测试应用 appId = 1
        sendMessageRequestBody.setMobile(mobile);
        sendMessageRequestBody.setTemplateId(templateId);
        sendMessageRequestBody.setTemplateParam(templateParam);

        RequestCommand<SendMessageRequestBody> sendMessageRequest = new RequestCommand<>(sendMessageRequestBody);
        smsAdapterDispatcher.dispatchRequest(RequestCode.SEND_MESSAGE, sendMessageRequest);
        return BaseModel.getInstance("success");
    }

}

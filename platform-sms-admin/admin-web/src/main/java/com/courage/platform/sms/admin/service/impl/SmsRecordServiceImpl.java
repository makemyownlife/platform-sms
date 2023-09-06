package com.courage.platform.sms.admin.service.impl;

import com.courage.platform.sms.admin.dao.TSmsAppinfoDAO;
import com.courage.platform.sms.admin.dao.TSmsChannelDAO;
import com.courage.platform.sms.admin.dao.TSmsRecordDAO;
import com.courage.platform.sms.admin.dao.TSmsRecordDetailDAO;
import com.courage.platform.sms.admin.dispatcher.SmsAdapterDispatcher;
import com.courage.platform.sms.admin.dispatcher.processor.RequestCode;
import com.courage.platform.sms.admin.dispatcher.processor.RequestEntity;
import com.courage.platform.sms.admin.dispatcher.processor.ResponseEntity;
import com.courage.platform.sms.admin.dispatcher.processor.body.SendMessageRequestBody;
import com.courage.platform.sms.admin.domain.TSmsAppinfo;
import com.courage.platform.sms.admin.domain.TSmsChannel;
import com.courage.platform.sms.admin.domain.TSmsRecordDetail;
import com.courage.platform.sms.admin.domain.vo.BaseModel;
import com.courage.platform.sms.admin.domain.vo.RecordVO;
import com.courage.platform.sms.admin.service.SmsRecordService;
import com.courage.platform.sms.client.SmsSenderResult;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SmsRecordServiceImpl implements SmsRecordService {

    private final Logger logger = LoggerFactory.getLogger(SmsRecordServiceImpl.class);

    @Autowired
    private TSmsRecordDAO recordDAO;

    @Autowired
    private TSmsAppinfoDAO appinfoDAO;

    @Autowired
    private TSmsChannelDAO channelDAO;

    @Autowired
    private TSmsRecordDetailDAO detailDAO;

    @Autowired
    private SmsAdapterDispatcher smsAdapterDispatcher;

    @Override
    public List<TSmsRecordDetail> queryRecordDetailList(Map<String, Object> param) {
        return detailDAO.queryRecordDetailList(param);
    }

    @Override
    public List<RecordVO> queryRecordVOList(Map<String, Object> param) {
        // 此处代码为什么这么写，主要是向同学们展示批量操作 教学使用
        List<RecordVO> recordVOList = detailDAO.queryRecordVOList(param);
        if (CollectionUtils.isNotEmpty(recordVOList)) {
            List<Integer> appIdList = recordVOList.stream().map(RecordVO::getAppId).collect(Collectors.toList());                      // 应用ID列表
            List<Integer> channelIdList = recordVOList.stream().map(RecordVO::getChannelId).collect(Collectors.toList());         // 渠道ID列表
            List<TSmsAppinfo> smsAppinfoList = appinfoDAO.selectAppInfoListByIds(appIdList);                                     // 应用信息列表
            List<TSmsChannel> smsChannelList = channelDAO.selectChannelsByIds(channelIdList);
            Map<Long, TSmsAppinfo> appinfoMap = smsAppinfoList.stream().collect(Collectors.toMap(TSmsAppinfo::getId, appInfo -> appInfo));
            Map<Integer, TSmsChannel> channelMap = smsChannelList.stream().collect(Collectors.toMap(TSmsChannel::getId, tSmsChannel -> tSmsChannel));
            for (RecordVO recordVO : recordVOList) {
                recordVO.setChannelName(channelMap.get(recordVO.getChannelId()).getChannelName());
                recordVO.setAppName(appinfoMap.get(recordVO.getAppId()).getAppName());
            }
        }
        return recordVOList;
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
        templateParam = "{\"code\":\"1111\"}";
        sendMessageRequestBody.setTemplateParam(templateParam);

        RequestEntity<SendMessageRequestBody> sendMessageRequest = new RequestEntity<>(sendMessageRequestBody);
        smsAdapterDispatcher.dispatchSyncRequest(RequestCode.SEND_MESSAGE, sendMessageRequest);
        return BaseModel.getInstance("success");
    }

    public SmsSenderResult sendMessage(SendMessageRequestBody sendMessageRequestBody) {
        ResponseEntity<SmsSenderResult> processorResponse = smsAdapterDispatcher.dispatchSyncRequest(RequestCode.SEND_MESSAGE, new RequestEntity(sendMessageRequestBody));
        return processorResponse.getData();
    }

}

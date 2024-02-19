package cn.javayong.platform.sms.admin.service;

import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.body.SendMessageRequestBody;
import cn.javayong.platform.sms.admin.domain.vo.BaseModel;
import cn.javayong.platform.sms.admin.domain.vo.RecordVO;
import cn.javayong.platform.sms.admin.domain.TSmsRecordDetail;
import cn.javayong.platform.sms.client.SmsSenderResult;

import java.util.List;
import java.util.Map;

public interface SmsRecordService {

    List<TSmsRecordDetail> queryRecordDetailList(Map<String, Object> param);

    List<RecordVO> queryRecordVOList(Map<String, Object> param);

    List<RecordVO> queryOneRecordVOByAppId(String appId);

    Long queryCountRecordDetail(Map<String, Object> param);

    BaseModel<String> adminSendRecord(String mobile, String templateId, String templateParam);

    SmsSenderResult sendMessage(SendMessageRequestBody sendMessageRequestBody);

}

package cn.javayong.platform.sms.admin.dispatcher.processor.impl;

import cn.javayong.platform.sms.adapter.OuterAdapter;
import cn.javayong.platform.sms.adapter.command.req.SendSmsReqCommand;
import cn.javayong.platform.sms.adapter.command.resp.SmsRespCommand;
import cn.javayong.platform.sms.adapter.support.SmsTemplateUtil;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
import cn.javayong.platform.sms.admin.common.config.IdGenerator;
import cn.javayong.platform.sms.admin.common.utils.ResponseEntity;
import cn.javayong.platform.sms.admin.dao.TSmsRecordDAO;
import cn.javayong.platform.sms.admin.dao.TSmsRecordDetailDAO;
import cn.javayong.platform.sms.admin.dao.TSmsTemplateBindingDAO;
import cn.javayong.platform.sms.admin.dao.TSmsTemplateDAO;
import cn.javayong.platform.sms.admin.dispatcher.AdapterLoader;
import cn.javayong.platform.sms.admin.dispatcher.processor.AdatperProcessor;
import cn.javayong.platform.sms.admin.domain.TSmsRecord;
import cn.javayong.platform.sms.admin.domain.TSmsRecordDetail;
import cn.javayong.platform.sms.admin.domain.TSmsTemplate;
import cn.javayong.platform.sms.admin.domain.TSmsTemplateBinding;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 创建记录详情 真正的发送短信
 * Created by zhangyong on 2023/8/20.
 */
@Component
public class SendMessagelRequestProcessor implements AdatperProcessor<Long, List<Long>> {

    private static Logger logger = LoggerFactory.getLogger(SendMessagelRequestProcessor.class);

    @Autowired
    private TSmsRecordDAO smsRecordDAO;

    @Autowired
    private TSmsRecordDetailDAO detailDAO;

    @Autowired
    private TSmsTemplateDAO tSmsTemplateDAO;

    @Autowired
    private TSmsTemplateBindingDAO bindingDAO;

    @Autowired
    private AdapterLoader smsAdapterLoader;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseEntity<List<Long>> processRequest(RequestEntity<Long> requestCommand) {
        List<Long> detailIdList = new ArrayList<>();
        boolean sendFlag = false;                                                            // 成功 true/失败 false
        Long recordId = requestCommand.getData();
        TSmsRecord record = smsRecordDAO.selectByPrimaryKey(recordId);
        if (record != null) {
            if (record.getSendStatus() != -1) {
                logger.warn("短信记录 recordId:【" + recordId + "]已处理");
                return ResponseEntity.success(detailIdList);
            }
            Long templateId = record.getTemplateId();
            TSmsTemplate template = tSmsTemplateDAO.selectByPrimaryKey(templateId);
            List<TSmsTemplateBinding> bindingList = bindingDAO.selectBindingsByTemplateId(templateId);
            Collections.shuffle(bindingList);
            for (TSmsTemplateBinding tSmsTemplateBinding : bindingList) {
                Integer channelId = tSmsTemplateBinding.getChannelId();
                OuterAdapter outerAdapter = smsAdapterLoader.getAdapterByChannelId(channelId);
                if (outerAdapter != null) {
                    SendSmsReqCommand smsCommand = new SendSmsReqCommand();
                    smsCommand.setPhoneNumbers(record.getMobile());
                    smsCommand.setSignName(template.getSignName());
                    smsCommand.setTemplateContent(template.getContent());
                    smsCommand.setTemplateCode(tSmsTemplateBinding.getTemplateCode());
                    smsCommand.setTemplateParam(record.getTemplateParam());
                    SmsRespCommand<String> smsRespCommand = outerAdapter.sendSmsByTemplateId(smsCommand);
                    // 三方编号
                    String msgId = StringUtils.EMPTY;
                    if (smsRespCommand.getCode() == SmsRespCommand.SUCCESS_CODE) {
                        sendFlag = true;
                        msgId = (String) smsRespCommand.getData();
                    }
                    Long detailId = idGenerator.createUniqueId(String.valueOf(record.getAppId()));
                    // 插入详情表
                    TSmsRecordDetail detail = new TSmsRecordDetail();
                    detail.setId(detailId);
                    detail.setAppId(record.getAppId());
                    detail.setCreateTime(new Date());
                    detail.setUpdateTime(new Date());
                    detail.setMobile(record.getMobile());
                    detail.setChannelId(channelId);
                    detail.setErrorMsg(StringUtils.trimToEmpty(smsRespCommand.getMessage()));
                    detail.setRecordId(recordId);
                    detail.setErrorMsg(smsRespCommand.getMessage());
                    detail.setContent(SmsTemplateUtil.renderContentWithSignName(smsCommand.getTemplateParam(), smsCommand.getTemplateContent(), smsCommand.getSignName()));
                    detail.setMsgid(msgId);
                    detail.setSenderTime(new Date());
                    detail.setSendStatus(sendFlag ? 0 : 1);
                    logger.info("插入短信发送详情：" + JSON.toJSONString(detail));
                    detailDAO.insert(detail);
                    detailIdList.add(detailId);
                }
                if (sendFlag) {
                    break;
                }
            }
        }
        // 修改记录表状态
        TSmsRecord tSmsRecord = new TSmsRecord();
        tSmsRecord.setSendStatus(sendFlag ? 0 : 1);
        tSmsRecord.setUpdateTime(new Date());
        tSmsRecord.setId(recordId);
        smsRecordDAO.updateByPrimaryKeySelective(tSmsRecord);
        return ResponseEntity.success(detailIdList);
    }

}

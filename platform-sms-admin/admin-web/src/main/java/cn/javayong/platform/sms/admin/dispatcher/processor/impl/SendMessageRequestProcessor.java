package cn.javayong.platform.sms.admin.dispatcher.processor.impl;

import cn.javayong.platform.sms.admin.common.utils.UtilsAll;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.body.SendMessageRequestBody;
import cn.javayong.platform.sms.admin.common.config.IdGenerator;
import cn.javayong.platform.sms.admin.common.utils.RedisKeyConstants;
import cn.javayong.platform.sms.admin.common.utils.ResponseCode;
import cn.javayong.platform.sms.admin.common.utils.ResponseEntity;
import cn.javayong.platform.sms.admin.dao.TSmsRecordDAO;
import cn.javayong.platform.sms.admin.dao.TSmsTemplateDAO;
import cn.javayong.platform.sms.admin.dispatcher.AdapterSchedule;
import cn.javayong.platform.sms.admin.dispatcher.processor.AdatperProcessor;
import cn.javayong.platform.sms.admin.domain.TSmsRecord;
import cn.javayong.platform.sms.admin.domain.TSmsTemplate;
import cn.javayong.platform.sms.client.SmsSenderResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 发送短信处理器
 * Created by zhangyong on 2023/7/14.
 */
@Component
public class SendMessageRequestProcessor implements AdatperProcessor<SendMessageRequestBody, SmsSenderResult> {

    private static Logger logger = LoggerFactory.getLogger(SendMessageRequestProcessor.class);

    @Autowired
    private TSmsTemplateDAO templateDAO;

    @Autowired
    private TSmsRecordDAO smsRecordDAO;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private AdapterSchedule smsAdapterSchedule;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public ResponseEntity<SmsSenderResult> processRequest(RequestEntity<SendMessageRequestBody> processorRequest) {
        SendMessageRequestBody param = processorRequest.getData();
        String templateId = param.getTemplateId();
        TSmsTemplate tSmsTemplate = templateDAO.selectByPrimaryKey(Long.valueOf(templateId));
        if (tSmsTemplate == null) {
            return ResponseEntity.build(
                    ResponseCode.TEMPLATE_NOT_EXIST.getCode(),
                    ResponseCode.TEMPLATE_NOT_EXIST.getValue(),
                    new SmsSenderResult(ResponseCode.TEMPLATE_NOT_EXIST.getCode(), ResponseCode.TEMPLATE_NOT_EXIST.getValue())
            );
        }

        // 插入到记录 t_sms_record
        Long smsId = idGenerator.createUniqueId(String.valueOf(param.getAppId()));
        logger.info("appId:" + param.getAppId() + " smsId:" + smsId);
        TSmsRecord tSmsRecord = new TSmsRecord();
        tSmsRecord.setId(smsId);
        tSmsRecord.setAttime(param.getAttime());
        tSmsRecord.setTemplateId(Long.valueOf(param.getTemplateId()));
        tSmsRecord.setAppId(param.getAppId());
        tSmsRecord.setTemplateParam(param.getTemplateParam());
        tSmsRecord.setMobile(param.getMobile());
        tSmsRecord.setSendStatus(-1);
        Date currentDate = new Date();
        tSmsRecord.setUpdateTime(currentDate);
        tSmsRecord.setCreateTime(currentDate);
        smsRecordDAO.insertSelective(tSmsRecord);

        // 是否立即发送短信
        boolean createRecordDetailImmediately = true;

        // 延迟消息，假如是热数据，加入到延迟队列里
        if (StringUtils.isNotEmpty(param.getAttime())) {
            createRecordDetailImmediately = false;
            Long attime = Long.valueOf(param.getAttime());
            if (attime <= UtilsAll.getNextHourLastSecondTimestamp()) {
                // 两个自然小时的延迟短信，直接将数据添加到 Redis 的延迟队列  zset 容器
                smsAdapterSchedule.addDelayQueue(smsId, attime);
            }
        }

        // 立即发送的短信，调用立即发送短信线程池 执行任务 , 并放入到重试队列里 5 秒后检测
        if (createRecordDetailImmediately) {
            // 异步执行
            smsAdapterSchedule.executeNowCreateRecordDetail(smsId);
            // 立即发送短信的，将数据添加到 Redis 的 重试 zset 容器 ， 5 秒后做一个检测。
            smsAdapterSchedule.addRetryQueue(smsId, System.currentTimeMillis() + 5 * 1000);
        }

        SmsSenderResult smsSenderResult = new SmsSenderResult(String.valueOf(smsId));
        return ResponseEntity.success(smsSenderResult);
    }

}

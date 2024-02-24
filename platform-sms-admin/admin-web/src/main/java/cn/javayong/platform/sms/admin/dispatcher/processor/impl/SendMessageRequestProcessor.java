package cn.javayong.platform.sms.admin.dispatcher.processor.impl;

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

        Long currentTime = currentDate.getTime();
        if (StringUtils.isNotEmpty(param.getAttime())) {
            createRecordDetailImmediately = false;
            Long attime = Long.valueOf(param.getAttime());
            if (attime <= currentTime + 3600 * 1000) {
                // 1小时之内将发送的短信，直接将数据添加到 Redis 的 zset 容器
                redisTemplate.opsForZSet().add(RedisKeyConstants.WAITING_SEND_ZSET, String.valueOf(smsId), attime);
            }
        } else {
            // 立即发送短信的，将数据添加到 Redis 的 zset 容器
            redisTemplate.opsForZSet().add(RedisKeyConstants.WAITING_SEND_ZSET, String.valueOf(smsId), currentTime + 30 * 1000);
        }

        if(createRecordDetailImmediately) {
            // 异步执行
            smsAdapterSchedule.createRecordDetailImmediately(smsId);
        }

        SmsSenderResult smsSenderResult = new SmsSenderResult(String.valueOf(smsId));
        return ResponseEntity.success(smsSenderResult);
    }

}

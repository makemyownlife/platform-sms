package com.courage.platform.sms.core.impl;

import com.courage.platform.sms.core.ChannelSend;
import com.courage.platform.sms.core.domain.ChannelSendResult;
import com.courage.platform.sms.core.domain.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  亿美渠道
 */
public class EmaySingleChannelSend extends ChannelSend {

    private final static Logger logger = LoggerFactory.getLogger(EmaySingleChannelSend.class);

    public ChannelSendResult send(SmsMessage smsMessage) {
        ChannelSendResult channelSendResult = new ChannelSendResult();
        logger.info("调用亿美发送信息");
        try {
            //TODO 调用渠道方接口
            channelSendResult.setCode(ChannelSendResult.SUCCESS_CODE);
        } catch (Exception e) {
            logger.error("doSend error:", e);
            channelSendResult.setCode(ChannelSendResult.FAIL_CODE);
            channelSendResult.setMsg(e.getMessage());
        }
        return channelSendResult;
    }
}


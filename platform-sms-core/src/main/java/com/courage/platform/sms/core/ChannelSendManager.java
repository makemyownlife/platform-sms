package com.courage.platform.sms.core;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.core.domain.ChannelSendResult;
import com.courage.platform.sms.core.domain.SmsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 渠道短信管理器
 */
public class ChannelSendManager {

    private final static Logger logger = LoggerFactory.getLogger(ChannelSendManager.class);

    private static ChannelSendLoader channelSendLoader = new ChannelSendLoader();

    //发送单条短信
    public  ChannelSendResult sendSingleChannelLbs(SmsMessage smsMessage) {
        ChannelSendResult channelSendResult = null;
        List<ChannelSend> singleChannelSendList = channelSendLoader.getSingleChannelSendList();
        //混淆顺序
        Collections.shuffle(singleChannelSendList);
        //根据order排序
        Collections.sort(singleChannelSendList);
        //按顺序发送，直到发送成功
        for (ChannelSend channelSend : singleChannelSendList) {
            try {
                channelSendResult = channelSend.send(smsMessage);
                logger.info("短信单发渠道:{} id:{} 短信内容:{} 短信结果:{}",
                        channelSend.getChannelName(),
                        channelSend.getChannelId(),
                        JSON.toJSONString(smsMessage),
                        JSON.toJSONString(channelSendResult));
                if (channelSendResult != null && channelSendResult.getCode() == ChannelSendResult.SUCCESS_CODE) {
                    smsMessage.setChannel(channelSend.getChannelId());
                    return channelSendResult;
                }
            } catch (Throwable e) {
                logger.error("doSend manager error: ", e);
            }
        }
        return channelSendResult;
    }

    public static ChannelSendResult sendMarketChannel(SmsMessage smsMessage) {
        int size = channelSendLoader.getMarketChannelSendList().size();
        List<Integer> indexList = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            indexList.add(i);
        }
        ChannelSendResult channelSendResult = null;
        //混淆顺序
        Collections.shuffle(indexList);
        List<ChannelSend> singleChannelSendList = channelSendLoader.getMarketChannelSendList();
        try {
            ChannelSend channelSend = singleChannelSendList.get(0);
            channelSendResult = channelSend.send(smsMessage);
            logger.info("短信营销渠道:{} id:{} 短信内容:{} 短信结果:{}",
                    channelSend.getChannelName(),
                    channelSend.getChannelId(),
                    JSON.toJSONString(smsMessage),
                    JSON.toJSONString(channelSendResult));
            if (channelSendResult != null && channelSendResult.getCode() == ChannelSendResult.SUCCESS_CODE) {
                smsMessage.setChannel(channelSend.getChannelId());
                return channelSendResult;
            }
        } catch (Throwable e) {
            logger.error("doSend manager error: ", e);
        }

        return channelSendResult;
    }

    public static ChannelSendResult sendAppointedChannel(SmsMessage smsMessage) {
        //判断渠道是否为逗号分隔的多条渠道形式
        String[] channel = smsMessage.getChannel().split(",");
        List<String> channelIdList = new ArrayList<String>(channel.length);
        for (String channelId:channel){
            channelIdList.add(channelId);
        }
        ChannelSendResult channelSendResult = null;
        Collections.shuffle(channelIdList);
        for (String channelId : channelIdList) {
            try {
                ChannelSend channelSend = channelSendLoader.getChannelSendById(channelId);
                channelSendResult = channelSend.send(smsMessage);
                logger.info("短信营销渠道:{} id:{} 短信内容:{} 短信结果:{}",
                        channelSend.getChannelName(),
                        channelSend.getChannelId(),
                        JSON.toJSONString(smsMessage),
                        JSON.toJSONString(channelSendResult));
                if (channelSendResult != null && channelSendResult.getCode() == ChannelSendResult.SUCCESS_CODE) {
                    smsMessage.setChannel(channelSend.getChannelId());
                    return channelSendResult;
                }
            } catch (Throwable e) {
                logger.error("doSend manager error: ", e);
            }

        }
        return channelSendResult;
    }

    public static ChannelSendResult doSendSmsMessage(SmsMessage smsMessage) {
        //指定渠道 若发送失败 则失败
        if (StringUtils.isNotEmpty(smsMessage.getChannel())) {
            return sendAppointedChannel(smsMessage);
        }
        if (SmsTypeEnum.SINGLE.getIndex() == smsMessage.getType()) {
            return sendSingleChannelLbs(smsMessage);
        }
        if (SmsTypeEnum.MARKET.getIndex() == smsMessage.getType()) {
            return sendMarketChannel(smsMessage);
        }
        return null;
    }

}

package com.courage.platform.sms.server.controller;

import com.courage.platform.sms.client.SmsSenderResult;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class SenderController {

    private final static Logger logger = LoggerFactory.getLogger(SenderController.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.topic}")
    private String smsTopic;

    @RequestMapping("/sendSingle")
    @ResponseBody
    public SmsSenderResult sendSingle(HttpServletRequest request) {
        //接收请求参数
        String q = request.getParameter("q");
        String appKey = request.getParameter("appKey");
        String time = request.getParameter("time");
        logger.info("q:" + q + " appKey:" + appKey + " time:" + time);
        //发送消息
        String tag = "single";
        SendResult sendResult = rocketMQTemplate.syncSend(
                smsTopic + ":" + tag,
                MessageBuilder.withPayload(q).setHeader(MessageConst.PROPERTY_KEYS, time).build()
        );
        logger.info("sendResult:" + sendResult);
        if (sendResult != null) {
            if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                return new SmsSenderResult(sendResult.getMsgId());
            }
        }
        return new SmsSenderResult(SmsSenderResult.FAIL_CODE, "发送失败");
    }

}

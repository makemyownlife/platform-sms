package com.courage.platform.sms.worker.job;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyong on 2023/1/5.
 */
@Component
@RocketMQMessageListener(
        consumerGroup = "${rocketmq.consumer1.group}",  // 消费组，格式：namespace全称%group名称
        // 需要使用topic全称，所以进行topic名称的拼接，也可以自己设置  格式：namespace全称%topic名称
        topic = "${rocketmq.consumer1.topic}")
public class SmsMessageConsumer implements RocketMQListener<String> {

    private final static Logger logger = LoggerFactory.getLogger(SmsMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        logger.info("message:" + message);
        //从每个应用选择正常的短信渠道发送 短信，并将消息存储记录在 rocksdb 里 ，然后异步线程存储在数据库里
    }

}

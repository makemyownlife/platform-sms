package com.courage.platform.sms.worker.job;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

/**
 * 营销类短信消费者
 * Created by zhangyong on 2023/1/5.
 */
@Component
@DependsOn(value = {"smsAdapterService"})
@RocketMQMessageListener(
        consumerGroup = "${rocketmq.consumer2.group}",  // 消费组，格式：namespace全称%group名称
        // 需要使用topic全称，所以进行topic名称的拼接，也可以自己设置  格式：namespace全称%topic名称
        topic = "${rocketmq.consumer2.topic}",
        consumeThreadNumber = 4
)
public class SmsMessageMarketConsumer implements RocketMQListener<MessageExt> {

    private final static Logger logger = LoggerFactory.getLogger(SmsMessageMarketConsumer.class);

    public void onMessage(MessageExt message) {
        try {
            String body = new String(message.getBody(), "UTF-8");
            logger.info("营销短信:" + body);
            //从每个应用选择正常的短信渠道发送 短信，并将消息存储记录在 rocksdb 里 ，然后异步线程存储在数据库里
            // 1 将消息转换成DTO对象
            // 2 处理器处理逻辑：获取可以发送的渠道(随机算法)，构造发送请求对象，最后发送到渠道
        } catch (Exception e) {
            logger.error("market onMessage error:", e);
        }
    }

}

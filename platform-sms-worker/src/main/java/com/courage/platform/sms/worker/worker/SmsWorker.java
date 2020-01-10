package com.courage.platform.sms.worker.worker;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.core.domain.ChannelSendResult;
import com.courage.platform.sms.core.domain.SmsMessage;
import com.courage.platform.sms.worker.service.SmsRecordService;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SmsWorker {

    private final static Logger logger = LoggerFactory.getLogger(SmsWorker.class);

    //短信主题
    private final static String SMS_TOPIC = "platform_sms_topic";

    private final static ThreadPoolExecutor businessThreadPoolExecutor = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() * 2 + 1,
            Runtime.getRuntime().availableProcessors() * 2 + 1,
            100,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(50000),
            new ThreadFactory() {
                private AtomicInteger threadIndex = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LogicThreadPoolsExecutor_" + this.threadIndex.incrementAndGet());
                }
            },
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    @Resource(name = "mqConsumer")
    private DefaultMQPushConsumer defaultMQPushConsumer;

    @Autowired
    private SmsRecordService smsRecordService;

    public void start() {
        logger.info("短信消费者服务开始启动");
        try {
            defaultMQPushConsumer.subscribe(SMS_TOPIC, "*");
            defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            defaultMQPushConsumer.setMessageModel(MessageModel.CLUSTERING);
            defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                    for (Message msg : msgs) {
                        try {
                            SmsMessage smsMessage = JSON.parseObject(msg.getBody(), SmsMessage.class);
                            logger.info("消费者接受到的消息：" + smsMessage.toString());
                            ChannelSendResult sendResult = sendMsg(smsMessage);
                            asynAddSmsRecord(smsMessage, sendResult);
                        } catch (Exception e) {
                            logger.error("消费RocketMQ失败", e);
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            });
            defaultMQPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
        logger.info("Consumer Started.");
    }

    /*发送单条短信：*/
    private ChannelSendResult sendMsg(final SmsMessage smsMessage) {
        ChannelSendResult sendResult = null;//发送短信返回结果
        try {
         //   sendResult = ChannelSendManager.doSendSmsMessage(smsMessage);
        } catch (Exception e) {
            logger.error("短信发送异常【" + JSON.toJSONString(smsMessage) + "】", e);
        }
        return sendResult;
    }

    //异步保存短信记录
    private void asynAddSmsRecord(final SmsMessage smsMessage, final ChannelSendResult sendResult) {
        businessThreadPoolExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    smsRecordService.addSmsRecord(smsMessage, sendResult);
                } catch (Exception e) {
                    logger.error("异步报错短信记录失败【" + JSON.toJSONString(smsMessage) + "】", e);
                }
            }
        });
    }

}

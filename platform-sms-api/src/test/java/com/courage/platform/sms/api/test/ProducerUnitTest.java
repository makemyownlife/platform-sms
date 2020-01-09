package com.courage.platform.sms.api.test;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by zhangyong on 2020/1/9.
 */
public class ProducerUnitTest {

    private static Logger logger = LoggerFactory.getLogger(ProducerUnitTest.class);

    private DefaultMQProducer defaultMQProducer;

    private String nameserver = "192.168.31.240:9876";

    private String DEMO_TOPIC = "demo_topic";

    @Before
    public void init() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer();
        this.defaultMQProducer.setNamesrvAddr(nameserver);
        this.defaultMQProducer.setProducerGroup("DemoProducer");
        this.defaultMQProducer.setSendMsgTimeout(10000);
        this.defaultMQProducer.start();
    }

    @Test
    public void sendMessage() throws Exception {
        Message message = new Message(DEMO_TOPIC, "single", UUID.randomUUID().toString(), "hello lilin".getBytes("UTF-8"));
        SendResult sendResult = defaultMQProducer.send(message);
        logger.info("sendResult:" + sendResult);
    }

}

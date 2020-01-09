package com.courage.platform.sms.api.test;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangyong on 2020/1/9.
 */
public class ProducerUnitTest {

    private static Logger logger = LoggerFactory.getLogger(ProducerUnitTest.class);

    private DefaultMQProducer defaultMQProducer;

    private String nameserver = "192.168.31.240:9876";

    @Before
    public void init() throws MQClientException {
        this.defaultMQProducer = new DefaultMQProducer();
        this.defaultMQProducer.setNamesrvAddr(nameserver);
        this.defaultMQProducer.setProducerGroup("DemoProducer");
        this.defaultMQProducer.start();
    }

}

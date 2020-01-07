package com.courage.platform.sms.api.controller;

import com.courage.platform.sms.api.domain.JsonResult;
import com.courage.platform.sms.api.service.SmsApiService;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 短信服务主入口
 */
@Controller
@RequestMapping("/main")
public class SmsMainController {

    private final static Logger logger = LoggerFactory.getLogger(SmsMainController.class);

    @Autowired
    private SmsApiService smsApiService;

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    //短信主题
    private final static String PLATFORM_SMS_TOPIC = "platform_sms_single_topic";

    private final static ThreadPoolExecutor retryThread = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000), new ThreadFactory() {
        private AtomicInteger threadIndex = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "RetryThreadPoolsExecutor_" + this.threadIndex.incrementAndGet());
        }
    }, new ThreadPoolExecutor.DiscardOldestPolicy());

    @RequestMapping("/singlesend/v1")
    @ResponseBody
    public JsonResult sendSingle(HttpServletRequest request) {
        return new JsonResult(null);
    }

    @RequestMapping("/marketsend/v1")
    @ResponseBody
    public JsonResult sendMarket(HttpServletRequest request) {
        return new JsonResult(null);
    }

}

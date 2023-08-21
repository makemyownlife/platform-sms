package com.courage.platform.sms.admin.dispatcher;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.admin.common.utils.Pair;
import com.courage.platform.sms.admin.common.utils.ThreadFactoryImpl;
import com.courage.platform.sms.admin.dao.TSmsChannelDAO;
import com.courage.platform.sms.admin.dao.domain.TSmsChannel;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorRequest;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorRequestCode;
import com.courage.platform.sms.admin.dispatcher.processor.ProcessorResponse;
import com.courage.platform.sms.admin.dispatcher.processor.impl.ApplyTemplateRequestProcessor;
import com.courage.platform.sms.admin.dispatcher.processor.impl.SendMessageRequestProcessor;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

@Component(value = "smsAdapterDispatcher")
public class SmsAdapterDispatcher {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterDispatcher.class);

    //处理器命令映射
    protected final HashMap<Integer/* request code */, Pair<SmsAdatperProcessor, ExecutorService>> processorTable = new HashMap<Integer, Pair<SmsAdatperProcessor, ExecutorService>>(64);

    private volatile boolean running = false;

    @Autowired
    private SmsAdapterSchedule smsAdapterSchedule;

    @Autowired
    private SendMessageRequestProcessor sendMessageRequestProcessor;

    @Autowired
    private ApplyTemplateRequestProcessor applyTemplateRequestProcessor;

    private ExecutorService createRecordDetailThread;

    @PostConstruct
    public synchronized void init() {
        if (running) {
            return;
        }
        logger.info("开始初始化短信适配器分发服务");
        long start = System.currentTimeMillis();
        // 定时任务
        smsAdapterSchedule.init();
        // 映射处理器
        this.createRecordDetailThread = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("createRecordDetailThread-"));
        registerProcessor(ProcessorRequestCode.SEND_MESSAGE, sendMessageRequestProcessor);                                      // 发送短信
        registerProcessor(ProcessorRequestCode.APPLY_TEMPLATE, applyTemplateRequestProcessor);                                  // 申请模版
        registerProcessor(ProcessorRequestCode.CREATE_RECORD_DETAIL, applyTemplateRequestProcessor, createRecordDetailThread); // 创建记录详情 (异步,使用单独的线程)
        logger.info("结束初始化短信适配器分发服务, 耗时：" + (System.currentTimeMillis() - start));
    }

    //分发处理请求
    public ProcessorResponse dispatchRequest(int requestCode, ProcessorRequest processorRequest) throws InterruptedException {
        Pair<SmsAdatperProcessor, ExecutorService> pair = processorTable.get(requestCode);
        SmsAdatperProcessor smsAdatperProcessor = pair.getObject1();
        ExecutorService executorService = pair.getObject2();
        if (executorService == null) {
            ProcessorResponse response = smsAdatperProcessor.processRequest(processorRequest);
            return response;
        }
        //异步操作
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await(5000, TimeUnit.SECONDS);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessorResponse response = smsAdatperProcessor.processRequest(processorRequest);
                } catch (Exception e) {
                    logger.error("processRequest error:", e);
                } finally {
                    countDownLatch.countDown();
                }
            }
        });
    }

    //异步处理请求
    public void invokeAsync(int requestCode, ProcessorRequest processorRequest) {
        SmsAdatperProcessor smsAdatperProcessor = PROCESSOR_MAPPING.get(requestCode);
        smsAdatperProcessor.processRequest(processorRequest);
    }

    public void registerProcessor(int requestCode, SmsAdatperProcessor smsAdatperProcessor) {
        registerProcessor(requestCode, smsAdatperProcessor, null);
    }

    public void registerProcessor(int requestCode, SmsAdatperProcessor smsAdatperProcessor, ExecutorService executorService) {
        Pair<SmsAdatperProcessor, ExecutorService> pair = new Pair<SmsAdatperProcessor, ExecutorService>(smsAdatperProcessor, executorService);
        this.processorTable.put(requestCode, pair);
    }

    @PreDestroy
    public synchronized void destroy() {
        if (running) {
            long start = System.currentTimeMillis();
            logger.info("开始销毁短信适配器分发服务");
            //1.卸载所有的渠道
            smsAdapterSchedule.destroy();
            //2.关闭所有的命令处理器
            running = false;
            logger.info("结束销毁短信适配器分发服务, 耗时：" + (System.currentTimeMillis() - start));
        }
    }

}

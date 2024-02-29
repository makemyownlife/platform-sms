package cn.javayong.platform.sms.admin.dispatcher;

import cn.javayong.platform.sms.admin.dispatcher.processor.AdatperProcessor;
import cn.javayong.platform.sms.admin.dispatcher.processor.impl.ApplyTemplateRequestProcessor;
import cn.javayong.platform.sms.admin.dispatcher.processor.impl.SendMessagelRequestProcessor;
import cn.javayong.platform.sms.admin.dispatcher.processor.impl.CreateSmsRecordRequestProcessor;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestCode;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
import cn.javayong.platform.sms.admin.common.utils.Pair;
import cn.javayong.platform.sms.admin.common.utils.ResponseEntity;
import cn.javayong.platform.sms.admin.common.utils.ThreadFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * 请求分发器
 */
@Component(value = "smsAdapterDispatcher")
public class AdapterDispatcher {

    private final static Logger logger = LoggerFactory.getLogger(AdapterDispatcher.class);

    //处理器命令映射
    protected final HashMap<Integer/* request code */, Pair<AdatperProcessor, ExecutorService>> processorTable = new HashMap<Integer, Pair<AdatperProcessor, ExecutorService>>(64);

    private volatile boolean running = false;

    // 接收请求后，先存储短信记录 不与三方渠道交互
    @Autowired
    private CreateSmsRecordRequestProcessor createSmsRecordRequestProcessor;

    @Autowired
    private ApplyTemplateRequestProcessor applyTemplateRequestProcessor;

    // 调用三方渠道接口发送短信
    @Autowired
    private SendMessagelRequestProcessor sendMessagelRequestProcessor;

    private ExecutorService nowCreateRecordDetailThreads;

    private ExecutorService delayCreateRecordDetailThreads;

    @PostConstruct
    public synchronized void init() {
        if (running) {
            return;
        }
        logger.info("开始初始化短信适配器分发服务");
        long start = System.currentTimeMillis();
        // 立即发送线程池
        this.nowCreateRecordDetailThreads = new ThreadPoolExecutor(
                16,
                16,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000),
                new ThreadFactoryImpl("nowCreateRecordDetailThread-"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        // 延迟短信发送线程池
        this.delayCreateRecordDetailThreads = new ThreadPoolExecutor(
                8,
                8,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000),
                new ThreadFactoryImpl("delayCreateRecordDetailThread-"),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // 映射处理器
        registerProcessor(RequestCode.CREATE_SMS_RECORD_MESSAGE, createSmsRecordRequestProcessor);                                           // 发送短信
        registerProcessor(RequestCode.APPLY_TEMPLATE, applyTemplateRequestProcessor);                                       // 申请模版
        registerProcessor(RequestCode.NOW_CREATE_RECORD_DETAIL, sendMessagelRequestProcessor, nowCreateRecordDetailThreads); // 立即创建记录详情 ，异步调用三方接口 (使用单独的线程)
        registerProcessor(RequestCode.DELAY_CREATE_RECORD_DETAIL, sendMessagelRequestProcessor, delayCreateRecordDetailThreads); // 延迟创建记录详情 ，异步调用三方接口 (使用单独的线程)
        logger.info("结束初始化短信适配器分发服务, 耗时：" + (System.currentTimeMillis() - start));
    }

    // 分发处理请求
    public ResponseEntity dispatchSyncRequest(int requestCode, RequestEntity processorRequest) {
        Pair<AdatperProcessor, ExecutorService> pair = processorTable.get(requestCode);
        AdatperProcessor smsAdatperProcessor = pair.getObject1();
        ExecutorService executorService = pair.getObject2();
        if (executorService == null) {
            ResponseEntity responseCommand = smsAdatperProcessor.processRequest(processorRequest);
            return responseCommand;
        } else {
            Pair<CountDownLatch, ResponseEntity> responsePair = new Pair(new CountDownLatch(1), null);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ResponseEntity response = null;
                    try {
                        response = smsAdatperProcessor.processRequest(processorRequest);
                        responsePair.setObject2(response);
                    } catch (Throwable e) {
                        logger.error("dispatchSyncRequest processRequest error:", e);
                    } finally {
                        responsePair.getObject1().countDown();
                    }
                }
            });
            try {
                responsePair.getObject1().await(5000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
            return responsePair.getObject2();
        }
    }

    public void dispatchAsyncRequest(int requestCode, RequestEntity processorRequest) {
        Pair<AdatperProcessor, ExecutorService> pair = processorTable.get(requestCode);
        AdatperProcessor smsAdatperProcessor = pair.getObject1();
        ExecutorService executorService = pair.getObject2();
        if (executorService == null) {
            smsAdatperProcessor.processRequest(processorRequest);
        }
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    smsAdatperProcessor.processRequest(processorRequest);
                } catch (Throwable e) {
                    logger.error("dispatchAsyncRequest processRequest error:", e);
                }
            }
        });
    }

    public void registerProcessor(int requestCode, AdatperProcessor smsAdatperProcessor) {
        registerProcessor(requestCode, smsAdatperProcessor, null);
    }

    public void registerProcessor(int requestCode, AdatperProcessor adatperProcessor, ExecutorService executorService) {
        Pair<AdatperProcessor, ExecutorService> pair = new Pair<AdatperProcessor, ExecutorService>(adatperProcessor, executorService);
        this.processorTable.put(requestCode, pair);
    }

    @PreDestroy
    public synchronized void destroy() {
        if (running) {
            long start = System.currentTimeMillis();
            logger.info("开始销毁短信适配器分发服务");
            try {
                nowCreateRecordDetailThreads.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
            try {
                delayCreateRecordDetailThreads.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }
            running = false;
            logger.info("结束销毁短信适配器分发服务, 耗时：" + (System.currentTimeMillis() - start));
        }
    }

}

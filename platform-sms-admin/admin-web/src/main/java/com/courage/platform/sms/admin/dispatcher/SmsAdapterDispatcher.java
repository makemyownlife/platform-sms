package com.courage.platform.sms.admin.dispatcher;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
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
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component(value = "smsAdapterDispatcher")
public class SmsAdapterDispatcher {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterDispatcher.class);

    //处理器命令映射
    private static final ConcurrentHashMap<Integer, SmsAdatperProcessor> PROCESSOR_MAPPING = new ConcurrentHashMap<Integer, SmsAdatperProcessor>();

    private volatile boolean running = false;

    @Autowired
    private SmsAdapterSchedule smsAdapterSchedule;

    @Autowired
    private SendMessageRequestProcessor sendMessageRequestProcessor;

    @Autowired
    private ApplyTemplateRequestProcessor applyTemplateRequestProcessor;

    @PostConstruct
    public synchronized void init() {
        if (running) {
            return;
        }
        logger.info("开始初始化短信适配器分发服务");
        long start = System.currentTimeMillis();
        //初始化定时任务
        smsAdapterSchedule.init();
        //初始化映射处理器
        PROCESSOR_MAPPING.put(ProcessorRequestCode.SEND_MESSAGE, sendMessageRequestProcessor);
        PROCESSOR_MAPPING.put(ProcessorRequestCode.APPLY_TEMPLATE, applyTemplateRequestProcessor);
        logger.info("结束初始化短信适配器分发服务, 耗时：" + (System.currentTimeMillis() - start));
    }

    //处理短信网关请求
    public ProcessorResponse dispatchRequest(int requestCode, ProcessorRequest processorRequest) {
        SmsAdatperProcessor smsAdatperProcessor = PROCESSOR_MAPPING.get(requestCode);
        ProcessorResponse response = smsAdatperProcessor.processRequest(processorRequest);
        return response;
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

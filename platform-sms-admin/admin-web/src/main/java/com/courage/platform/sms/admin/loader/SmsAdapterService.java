package com.courage.platform.sms.admin.loader;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.admin.common.ThreadFactoryImpl;
import com.courage.platform.sms.admin.dao.TSmsChannelDAO;
import com.courage.platform.sms.admin.domain.TSmsChannel;
import com.courage.platform.sms.admin.loader.processors.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processors.ProcessorRequestCode;
import com.courage.platform.sms.admin.loader.processors.ProcessorResponse;
import com.courage.platform.sms.admin.loader.processors.impl.ApplyTemplateRequestProcessor;
import com.courage.platform.sms.admin.loader.processors.impl.SendMessageRequestProcessor;
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

@Component(value = "smsAdapterService")
public class SmsAdapterService {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterService.class);

    private volatile boolean running = false;

    @Autowired
    private TSmsChannelDAO smsChannelDAO;

    @Autowired
    private SmsAdapterLoader smsAdapterLoader;

    // 线程池
    private ScheduledExecutorService adapterExecutorService;

    @Autowired
    private SendMessageRequestProcessor sendMessageRequestProcessor;

    @Autowired
    private ApplyTemplateRequestProcessor applyTemplateRequestProcessor;

    // 渠道信息
    private static final ConcurrentHashMap<Integer, TSmsChannel> CHANNEL_MAPPING = new ConcurrentHashMap<Integer, TSmsChannel>();

    // 处理器命令映射
    private static final ConcurrentHashMap<Integer, SmsAdatperProcessor> PROCESSOR_MAPPING = new ConcurrentHashMap<Integer, SmsAdatperProcessor>();

    @PostConstruct
    public synchronized void init() {
        if (running) {
            return;
        }
        long start = System.currentTimeMillis();
        // 初始化定时线程池
        this.adapterExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryImpl("adapterExecutorService"));
        logger.info("开始初始化短信适配器服务");
        adapterExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    List<TSmsChannel> channelList = smsChannelDAO.queryChannels(MapUtils.EMPTY_MAP);
                    for (TSmsChannel tSmsChannel : channelList) {
                        TSmsChannel prewChannel = CHANNEL_MAPPING.get(tSmsChannel.getId());
                        boolean needLoadPlugin = false;
                        if ((prewChannel != null && !prewChannel.getMd5Value().equals(tSmsChannel.getMd5Value())) || (prewChannel == null)) {
                            needLoadPlugin = true;
                        }
                        if (needLoadPlugin) {
                            logger.info("开始加载渠道：" + JSON.toJSONString(tSmsChannel));
                            SmsChannelConfig channelConfig = new SmsChannelConfig();
                            BeanUtils.copyProperties(tSmsChannel, channelConfig);
                            smsAdapterLoader.loadAdapter(channelConfig);
                            logger.info("结束加载渠道 ，渠道编号" + tSmsChannel.getId());
                        }
                        CHANNEL_MAPPING.put(tSmsChannel.getId(), tSmsChannel);
                    }
                } catch (Exception e) {
                    logger.error("加载渠道信息出错 ：", e);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);
        PROCESSOR_MAPPING.put(ProcessorRequestCode.SEND_MESSAGE, sendMessageRequestProcessor);
        PROCESSOR_MAPPING.put(ProcessorRequestCode.APPLY_TEMPLATE, applyTemplateRequestProcessor);
        logger.info("结束初始化短信适配器服务, 耗时：" + (System.currentTimeMillis() - start));
    }

    // 处理短信网关请求
    public ProcessorResponse processRequest(int requestCode, ProcessorRequest processorRequest) {
        SmsAdatperProcessor smsAdatperProcessor = PROCESSOR_MAPPING.get(requestCode);
        ProcessorResponse response = smsAdatperProcessor.processRequest(processorRequest);
        return response;
    }

    @PreDestroy
    public synchronized void destroy() {
        if (running) {
            long start = System.currentTimeMillis();
            logger.info("开始销毁短信适配器服务");
            //1.卸载所有的渠道
            //2.关闭所有的命令处理器
            running = false;
            logger.info("结束销毁短信适配器服务, 耗时：" + (System.currentTimeMillis() - start));
        }
    }

}

package com.courage.platform.sms.admin.dispatcher;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.admin.common.utils.ThreadFactoryImpl;
import com.courage.platform.sms.admin.dao.TSmsChannelDAO;
import com.courage.platform.sms.admin.domain.TSmsChannel;
import com.courage.platform.sms.admin.dispatcher.processor.requeset.RequestCode;
import com.courage.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
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

/**
 * 适配器定时任务
 */
@Component
public class AdapterSchedule {

    private final static Logger logger = LoggerFactory.getLogger(AdapterSchedule.class);

    //渠道信息
    private static final ConcurrentHashMap<Integer, TSmsChannel> CHANNEL_MAPPING = new ConcurrentHashMap<Integer, TSmsChannel>();

    private final static int INIT_DELAY = 0;

    private final static int PERIOD = 5;

    //线程池
    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    private AdapterLoader smsAdapterLoader;

    @Autowired
    private TSmsChannelDAO smsChannelDAO;

    @Autowired
    private AdapterDispatcher smsAdapterDispatcher;

    @PostConstruct
    public synchronized void init() {
        //初始化定时线程池
        this.scheduledExecutorService = Executors.newScheduledThreadPool(2, new ThreadFactoryImpl("adapterScheduledThread-"));
        //定时加载适配器
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                scheudleLoadAdapter();
            }
        }, INIT_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    // 定时加载适配器
    private void scheudleLoadAdapter() {
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
        } catch (Throwable e) {
            logger.error("加载渠道信息出错 ：", e);
        }
    }

    public void createRecordDetailImmediately(Long recordId) {
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestEntity<Long> requestEntity = new RequestEntity<>(recordId);
                    smsAdapterDispatcher.dispatchAsyncRequest(RequestCode.CREATE_RECORD_DETAIL, requestEntity);
                } catch (Throwable e) {
                    logger.error("schedule createRecordDetailImmediately error:", e);
                }
            }
        }, 0, TimeUnit.SECONDS);
    }

    @PreDestroy
    public synchronized void destroy() {
        this.scheduledExecutorService.shutdown();
    }

}
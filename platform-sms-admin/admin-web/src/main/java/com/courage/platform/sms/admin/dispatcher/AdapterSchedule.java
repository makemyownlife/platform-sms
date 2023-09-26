package com.courage.platform.sms.admin.dispatcher;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.admin.common.utils.RedisKeyConstants;
import com.courage.platform.sms.admin.common.utils.ThreadFactoryImpl;
import com.courage.platform.sms.admin.dao.TSmsChannelDAO;
import com.courage.platform.sms.admin.dispatcher.processor.requeset.RequestCode;
import com.courage.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
import com.courage.platform.sms.admin.domain.TSmsChannel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;
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

    private final static int SCHEDULE_THREAD_COUNT = 4;

    private final static int INIT_DELAY = 0;

    private final static int PERIOD = 5;

    // 线程池
    private ScheduledExecutorService scheduledExecutorService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private AdapterLoader smsAdapterLoader;

    @Autowired
    private TSmsChannelDAO smsChannelDAO;

    @Autowired
    private AdapterDispatcher smsAdapterDispatcher;

    // 延迟服务是否启动
    private volatile boolean delayServiceRunning = false;

    // 延迟服务通知对象
    private Object notifyObject = new Object();

    // 延迟处理线程
    private Thread delayThread;

    @PostConstruct
    public synchronized void init() {
        //初始化定时线程池
        this.scheduledExecutorService = Executors.newScheduledThreadPool(SCHEDULE_THREAD_COUNT, new ThreadFactoryImpl("adapterScheduledThread-"));
        //定时加载适配器
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                scheudleLoadAdapter();
            }
        }, INIT_DELAY, PERIOD, TimeUnit.SECONDS);
        //启动延迟服务
        startDelayThread();
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
            logger.error("加载渠道信息出错：", e);
        }
    }

    private synchronized void startDelayThread() {
        if (delayServiceRunning) {
            return;
        }
        delayServiceRunning = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (delayServiceRunning) {
                    synchronized (notifyObject) {
                        try {
                            // 1. 首先通过redis的setnx命令 ，添加分布式锁
                            // 2. 判断zset的第一个元素是否需要执行时，若到期了，则取出来，并放入到生成详情的消费者执行
                            // 3. 从zset中删除该短信记录
                            // 4. 删除分布式锁
                            redisTemplate.opsForValue().setIfAbsent(RedisKeyConstants.WAITING_SEND_LOCK, "1", 1000L, TimeUnit.MILLISECONDS);
                            Set<ZSetOperations.TypedTuple<String>> recordIds = redisTemplate.opsForZSet().rangeWithScores(RedisKeyConstants.WAITING_SEND_ZSET, 0, 1);
                            if (CollectionUtils.isNotEmpty(recordIds)) {
                                ZSetOperations.TypedTuple<String> recordIdTuple = recordIds.iterator().next();
                                String recordId = recordIdTuple.getValue();
                                Long triggerTime = recordIdTuple.getScore().longValue();
                                logger.info("短信记录recordId:" + recordId + " triggerTime:" + triggerTime);
                                if (StringUtils.isNotEmpty(recordId)) {
                                    Long currentTime = System.currentTimeMillis();
                                    if (currentTime - triggerTime >= 0) {
                                        logger.info("短信记录recordId:" + recordId + " 可以发送了");
                                        createRecordDetailImmediately(Long.valueOf(recordId));
                                        redisTemplate.opsForZSet().remove(RedisKeyConstants.WAITING_SEND_ZSET, String.valueOf(recordId));
                                    } else {
                                        long diff = triggerTime - currentTime;
                                        logger.info("短信记录recordId:" + recordId + " 需要等待:" + diff);
                                        notifyObject.wait(diff);
                                    }
                                }
                            } else {
                                // 默认休眠100毫秒
                                notifyObject.wait(100L);
                            }
                        } catch (Exception e) {
                            logger.error("delayService error: ", e);
                        } finally {
                            try {
                                redisTemplate.delete(RedisKeyConstants.WAITING_SEND_LOCK);
                            } catch (Exception e) {
                                logger.error("redisTemplate delete key error:", e);
                            }
                        }
                    }
                }
            }
        };
        this.delayThread = new Thread(runnable, "delayThread");
        this.delayThread.start();
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
        if (this.delayServiceRunning) {
            this.delayServiceRunning = false;
        }
    }

}
package cn.javayong.platform.sms.admin.dispatcher;

import cn.javayong.platform.sms.admin.common.utils.Pair;
import cn.javayong.platform.sms.admin.common.utils.UtilsAll;
import cn.javayong.platform.sms.admin.dao.TSmsRecordDAO;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestCode;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
import com.alibaba.fastjson.JSON;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import cn.javayong.platform.sms.admin.common.utils.RedisKeyConstants;
import cn.javayong.platform.sms.admin.dao.TSmsChannelDAO;
import cn.javayong.platform.sms.admin.domain.TSmsChannel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;

/**
 * 适配器定时任务
 */
@Component
public class AdapterScheduler {

    private final static Logger logger = LoggerFactory.getLogger(AdapterScheduler.class);

    //渠道信息
    private static final ConcurrentHashMap<Integer, TSmsChannel> CHANNEL_MAPPING = new ConcurrentHashMap<Integer, TSmsChannel>();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AdapterLoader smsAdapterLoader;

    @Autowired
    private TSmsRecordDAO smsRecordDAO;

    @Autowired
    private TSmsChannelDAO smsChannelDAO;

    @Autowired
    private AdapterDispatcher smsAdapterDispatcher;

    // 加载适配器任务是否运行
    private volatile boolean loadAdapterRunning = false;

    // 延迟服务是否启动
    private volatile boolean delayServiceRunning = false;

    // 重试服务是否启动
    private volatile boolean retryRuning = false;

    // 加载下个自然小时的延迟短信任务开关
    private volatile boolean loadNextHourTaskRunning = false;

    private final static Long DEFAULT_DELAY_WAIT_TIME = 300L;

    // 延迟服务通知对象
    private static Object notifyObject = new Object();

    @PostConstruct
    public synchronized void init() {
        // 启动加载适配器服务
        startLoadAdapterThread();
        // 启动重试服务
        startRetryThread();
        // 启动延迟服务
        startDelayThread();
        // 启动任务：加载下个小时延迟短信，并放入到 ZSET 集合
        startLoadNextHourRecord();
    }

    private synchronized void startLoadAdapterThread() {
        logger.info("开始加载适配器列表");
        if (loadAdapterRunning) {
            return;
        }
        loadAdapterRunning = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (loadAdapterRunning) {
                    scheudleLoadAdapter();
                    try {
                        Thread.sleep(5000L);
                    } catch (Exception e) {
                    }
                }
            }
        };
        Thread loadAdapterThread = new Thread(runnable, "loadAdapterThread");
        loadAdapterThread.start();
        logger.info("结束加载适配器列表");
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
                // 1. 首先通过redis的setnx命令 ，添加分布式锁
                // 2. 判断zset的第一个元素是否需要执行时，若到期了，则取出来，并放入到生成详情的消费者执行
                // 3. 从zset中删除该短信记录
                // 4. 删除分布式锁
                while (delayServiceRunning) {
                    synchronized (notifyObject) {
                        boolean lockFlag = false;
                        long waitTime = DEFAULT_DELAY_WAIT_TIME;
                        try {
                            lockFlag = redisTemplate.opsForValue().setIfAbsent(RedisKeyConstants.DELAY_SEND_LOCK, 1, 60, TimeUnit.SECONDS);
                            if (lockFlag) {
                                Set<ZSetOperations.TypedTuple<Long>> recordIds = redisTemplate.opsForZSet().rangeWithScores(RedisKeyConstants.DELAY_SEND_ZSET, 0, 1);
                                if (CollectionUtils.isNotEmpty(recordIds)) {
                                    ZSetOperations.TypedTuple<Long> recordIdTuple = recordIds.iterator().next();
                                    Long recordId = recordIdTuple.getValue();
                                    Long triggerTime = recordIdTuple.getScore().longValue();
                                    Long currentTime = System.currentTimeMillis();
                                    if (currentTime - triggerTime >= 0) {
                                        executeDelaySendMessage(recordId);
                                        removeElementFromDelayQueue(recordId);
                                    } else {
                                        waitTime = triggerTime - currentTime;
                                        //logger.info("短信记录recordId:" + recordId + " 需要等待:" + waitTime);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("delayService error: ", e);
                        } finally {
                            if (lockFlag) {
                                try {
                                    redisTemplate.delete(RedisKeyConstants.DELAY_SEND_LOCK);
                                } catch (Exception e) {
                                    logger.error("redisTemplate delete key error:", e);
                                }
                            }
                        }
                        try {
                            notifyObject.wait(waitTime > DEFAULT_DELAY_WAIT_TIME ? DEFAULT_DELAY_WAIT_TIME : waitTime);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        };
        Thread delayThread = new Thread(runnable, "delayThread");
        delayThread.start();
        logger.info("启动延迟服务成功");
    }

    public synchronized void startRetryThread() {
        if (this.retryRuning) {
            return;
        }
        this.retryRuning = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (retryRuning) {
                    boolean lockFlag = false;
                    try {
                        lockFlag = redisTemplate.opsForValue().setIfAbsent(RedisKeyConstants.RETRY_SEND_LOCK, 1, 60, TimeUnit.SECONDS);
                        if (lockFlag) {
                            Set<ZSetOperations.TypedTuple<Long>> recordIds = redisTemplate.opsForZSet().rangeWithScores(RedisKeyConstants.RETRY_SEND_ZSET, 0, 1);
                            if (CollectionUtils.isNotEmpty(recordIds)) {
                                ZSetOperations.TypedTuple<Long> recordIdTuple = recordIds.iterator().next();
                                Long recordId = recordIdTuple.getValue();
                                Long triggerTime = recordIdTuple.getScore().longValue();
                                Long currentTime = System.currentTimeMillis();
                                if (currentTime - triggerTime >= 0) {
                                    executeNowSendMessage(recordId);
                                    removeElementFromRetryQueue(recordId);
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("retry Service error: ", e);
                    } finally {
                        if (lockFlag) {
                            try {
                                redisTemplate.delete(RedisKeyConstants.RETRY_SEND_LOCK);
                            } catch (Exception e) {
                                logger.error("redisTemplate delete retrylockkey error:", e);
                            }
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                    }
                }
            }
        };
        Thread retryThread = new Thread(runnable, "retryThread");
        retryThread.start();
        logger.info("启动重试服务成功");
    }

    // 每隔 10 分钟 将下个小时需要发送的延迟短信 加载到 redis 中 。
    public void startLoadNextHourRecord() {
        if (loadNextHourTaskRunning) {
            return;
        }
        loadNextHourTaskRunning = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (loadNextHourTaskRunning) {
                    boolean lockFlag = false;
                    Pair<Long, Long> pair = UtilsAll.getNextHourFirstAndLast();
                    String nextHour = DateFormatUtils.format(pair.getObject1(), "yyyyMMddHH");
                    try {
                        String loaded = (String) redisTemplate.opsForHash().get(RedisKeyConstants.LOAD_NEXT_HOUR_RESULT, nextHour);
                        if (loaded == null) {
                            Long nextHourFirstTimeStamp = pair.getObject1();
                            Long nextHourLastTimeStamp = pair.getObject2();
                            logger.info("startTime: {} endTime:{}",
                                    DateFormatUtils.format(new Date(nextHourFirstTimeStamp), "yyyy-MM-dd HH:mm:ss"),
                                    DateFormatUtils.format(new Date(nextHourLastTimeStamp), "yyyy-MM-dd HH:mm:ss")
                            );
                            lockFlag = redisTemplate.opsForValue().setIfAbsent(RedisKeyConstants.LOAD_NEXT_HOUR_LOCK, "1", 5, TimeUnit.MINUTES);
                            if (lockFlag) {
                                Long startId = null;
                                int count = 0;
                                for (; ; ) {
                                    List<Map> recordList = smsRecordDAO.queryWaitingSendSmsList(String.valueOf(nextHourFirstTimeStamp), String.valueOf(nextHourLastTimeStamp), startId);
                                    if (CollectionUtils.isEmpty(recordList)) {
                                        break;
                                    } else {
                                        if (CollectionUtils.isNotEmpty(recordList)) {
                                            count++;
                                            Set<ZSetOperations.TypedTuple<Long>> typedTupleSet = new HashSet<>();
                                            for (Map record : recordList) {
                                                ZSetOperations.TypedTuple<Long> typedTuple = new DefaultTypedTuple<Long>((Long) (record.get("id")), Double.valueOf((String) record.get("attime")));
                                                typedTupleSet.add(typedTuple);
                                                startId = (Long) record.get("id");
                                            }
                                            redisTemplate.opsForZSet().add(RedisKeyConstants.DELAY_SEND_ZSET, typedTupleSet);
                                        }
                                    }
                                }
                                logger.info("nextHour:" + nextHour + " 处理数据条数：" + count);
                                redisTemplate.opsForHash().put(RedisKeyConstants.LOAD_NEXT_HOUR_RESULT, nextHour, String.valueOf(count));
                            }
                        }
                    } catch (Throwable e) {
                        logger.error("load next hour record error:", e);
                    } finally {
                        if (lockFlag) {
                            try {
                                redisTemplate.delete(RedisKeyConstants.LOAD_NEXT_HOUR_LOCK);
                            } catch (Exception e) {
                                logger.error("redisTemplate delete loadnexthourlockkey error:", e);
                            }
                        }
                    }
                    // 每隔10分钟 ，执行一次 （并行情况下需要加锁 )）
                    try {
                        if (loadNextHourTaskRunning) {
                            Thread.sleep(10 * 1000 * 60);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        Thread loadNextHourRecordThread = new Thread(runnable, "loadNextHourRecordThread");
        loadNextHourRecordThread.start();
        logger.info("启动加载下个自然小时服务成功");
    }

    public void executeNowSendMessage(Long recordId) {
        try {
            // 执行线程池调用三方接口发送短信
            RequestEntity<Long> requestEntity = new RequestEntity<>(recordId);
            smsAdapterDispatcher.dispatchAsyncRequest(RequestCode.NOW_SEND_MESSAGE, requestEntity);
        } catch (Throwable e) {
            logger.error("executeNowCreateRecordDetailImmediately error:", e);
        }
    }

    private void executeDelaySendMessage(Long recordId) {
        try {
            // 执行线程池调用三方接口发送短信
            RequestEntity<Long> requestEntity = new RequestEntity<>(recordId);
            smsAdapterDispatcher.dispatchAsyncRequest(RequestCode.DELAY_SEND_MESSAGE, requestEntity);
        } catch (Throwable e) {
            logger.error("executeDelayCreateRecordDetail error:", e);
        }
    }

    public void addRetryQueue(Long recordId, Long time) {
        redisTemplate.opsForZSet().add(RedisKeyConstants.RETRY_SEND_ZSET, recordId, time);
    }

    private void removeElementFromRetryQueue(Long recordId) {
        redisTemplate.opsForZSet().remove(RedisKeyConstants.RETRY_SEND_ZSET, recordId);
    }

    public void addDelayQueue(Long recordId, Long time) {
        redisTemplate.opsForZSet().add(RedisKeyConstants.DELAY_SEND_ZSET, recordId, time);
    }

    public void removeElementFromDelayQueue(Long recordId) {
        redisTemplate.opsForZSet().remove(RedisKeyConstants.DELAY_SEND_ZSET, recordId);
    }

    @PreDestroy
    public synchronized void destroy() {
        if (this.delayServiceRunning) {
            this.delayServiceRunning = false;
        }
        if (this.retryRuning) {
            this.retryRuning = false;
        }
        if (this.loadAdapterRunning) {
            this.loadAdapterRunning = false;
        }
        if (this.loadNextHourTaskRunning) {
            this.loadNextHourTaskRunning = false;
        }
    }

}
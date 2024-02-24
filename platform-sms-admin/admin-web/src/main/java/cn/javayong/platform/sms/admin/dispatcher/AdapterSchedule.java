package cn.javayong.platform.sms.admin.dispatcher;

import cn.javayong.platform.sms.admin.common.utils.Pair;
import cn.javayong.platform.sms.admin.common.utils.UtilsAll;
import cn.javayong.platform.sms.admin.dao.TSmsRecordDAO;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestCode;
import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.RequestEntity;
import com.alibaba.fastjson.JSON;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import cn.javayong.platform.sms.admin.common.utils.RedisKeyConstants;
import cn.javayong.platform.sms.admin.common.utils.ThreadFactoryImpl;
import cn.javayong.platform.sms.admin.dao.TSmsChannelDAO;
import cn.javayong.platform.sms.admin.domain.TSmsChannel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private TSmsRecordDAO smsRecordDAO;

    @Autowired
    private TSmsChannelDAO smsChannelDAO;

    @Autowired
    private AdapterDispatcher smsAdapterDispatcher;

    // 延迟服务是否启动
    private volatile boolean delayServiceRunning = false;

    // 加载下个小时的延迟短信任务开关
    private volatile boolean loadNextHourTaskRunning = false;

    private Thread loadNextHourRecordThread;

    private final static Long DEFAULT_DELAY_WAIT_TIME = 100L;

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
        // 启动延迟服务
        startDelayThread();
        // 启动任务：加载下个小时延迟短信，并放入到 ZSET 集合
        startLoadNextHourRecord();
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
                            lockFlag = redisTemplate.opsForValue().setIfAbsent(RedisKeyConstants.WAITING_SEND_LOCK, "1", 3000, TimeUnit.MILLISECONDS);
                            if (lockFlag) {
                                Set<ZSetOperations.TypedTuple<String>> recordIds = redisTemplate.opsForZSet().rangeWithScores(RedisKeyConstants.WAITING_SEND_ZSET, 0, 1);
                                if (CollectionUtils.isNotEmpty(recordIds)) {
                                    ZSetOperations.TypedTuple<String> recordIdTuple = recordIds.iterator().next();
                                    String recordId = recordIdTuple.getValue();
                                    Long triggerTime = recordIdTuple.getScore().longValue();
                                    if (StringUtils.isNotEmpty(recordId)) {
                                        Long currentTime = System.currentTimeMillis();
                                        if (currentTime - triggerTime >= 0) {
                                            createRecordDetailImmediately(Long.valueOf(recordId));
                                            redisTemplate.opsForZSet().remove(RedisKeyConstants.WAITING_SEND_ZSET, String.valueOf(recordId));
                                        } else {
                                            waitTime = triggerTime - currentTime;
                                            logger.info("短信记录recordId:" + recordId + " 需要等待:" + waitTime);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            logger.error("delayService error: ", e);
                        } finally {
                            if (lockFlag) {
                                try {
                                    redisTemplate.delete(RedisKeyConstants.WAITING_SEND_LOCK);
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
        delayThread = new Thread(runnable, "delayThread");
        delayThread.start();
    }

    // 每隔 15 分钟 将下个小时需要发送的延迟短信 加载到 redis 中 。
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
                            Long nextHourLastTimeStamp = pair.getObject1();
                            Long nextHourFirstTimeStamp = pair.getObject2();
                            lockFlag = redisTemplate.opsForValue().setIfAbsent(RedisKeyConstants.LOAD_NEXT_HOUR_LOCK, "1", 10, TimeUnit.MINUTES);
                            if (lockFlag) {
                                Long startId = null;
                                for (; ; ) {
                                    List<Map> recordList = smsRecordDAO.queryWaitingSendSmsList(
                                            String.valueOf(nextHourFirstTimeStamp),
                                            String.valueOf(nextHourLastTimeStamp),
                                            startId);
                                    if (CollectionUtils.isEmpty(recordList)) {
                                        break;
                                    } else {
                                        if (CollectionUtils.isNotEmpty(recordList)) {
                                            Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
                                            for (Map record : recordList) {
                                                ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<String>(
                                                        String.valueOf(record.get("id")),
                                                        Double.valueOf((String) record.get("attime"))
                                                );
                                                typedTupleSet.add(typedTuple);
                                                startId = (Long) record.get("id");
                                            }
                                            redisTemplate.opsForZSet().add(RedisKeyConstants.WAITING_SEND_ZSET, typedTupleSet);
                                        }
                                    }
                                }
                                redisTemplate.opsForHash().put(RedisKeyConstants.LOAD_NEXT_HOUR_RESULT, nextHour, "1");
                            }
                        }
                    } catch (Throwable e) {
                        logger.error("load next hour record error:", e);
                    } finally {
                        if (lockFlag) {
                            redisTemplate.delete(RedisKeyConstants.LOAD_NEXT_HOUR_LOCK);
                        }
                    }
                    // 每隔15分钟 ，执行一次 （并行情况下需要加锁 )）
                    try {
                        if (loadNextHourTaskRunning) {
                            Thread.sleep(10 * 1000 * 60);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        loadNextHourRecordThread = new Thread(runnable, "loadNextHourRecordThread");
        loadNextHourRecordThread.start();
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
        if (this.loadNextHourTaskRunning) {
            this.loadNextHourTaskRunning = false;
        }
        if (this.delayServiceRunning) {
            this.delayServiceRunning = false;
        }
    }

}
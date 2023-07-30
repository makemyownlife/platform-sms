package com.courage.platform.sms.admin.common.config;

import com.courage.platform.sms.admin.common.sharding.LocalSequence;
import com.courage.platform.sms.admin.common.sharding.ShardingConstants;
import com.courage.platform.sms.admin.common.sharding.SnowFlakeIdGenerator;
import com.courage.platform.sms.admin.common.sharding.StringHashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 编号生成器
 */
@Component
public class IdGenerator {

    private final static Logger logger = LoggerFactory.getLogger(IdGenerator.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public Long createUniqueId(String shardingKey) {
        Integer workerId = StringHashUtil.hashSlot(shardingKey);
        Long currentTime = System.currentTimeMillis();
        // 从本地缓冲中获取
        LocalSequence.SequenceEntity sequenceEntity = LocalSequence.getSeqEntity();
        if (sequenceEntity != null) {
            return SnowFlakeIdGenerator.getUniqueId(sequenceEntity.getCurrentTime(), workerId, sequenceEntity.getSeq());
        }
        // 从redis自增一个步长 , 放入本地内存中待用
        String idGeneratorKey = ShardingConstants.ID_REDIS_PFEFIX + currentTime;
        Long counter = redisTemplate.opsForValue().increment(idGeneratorKey, ShardingConstants.STEP_LENGTH);
        // 设置过期时间：120秒
        redisTemplate.expire(idGeneratorKey, ShardingConstants.SEQ_EXPIRE_TIME, TimeUnit.SECONDS);
        logger.warn("redisKey:{} 序号值:{} ", idGeneratorKey, counter);
        // 判断是否有极限情况 ,1ms产生的数据超过了最大序号，那么最有可能原因是 当前机器的时间钟不一样
        if (counter - ShardingConstants.STEP_LENGTH >= ShardingConstants.MAX_SEQ) {
            logger.error("redisKey:{} 序号值:{} 超过了最大阈值{}", idGeneratorKey, counter, ShardingConstants.MAX_SEQ);
            return null;
        }
        // 当前自增的最小 id
        long cursor = counter - ShardingConstants.STEP_LENGTH + 1;
        while (cursor <= ShardingConstants.MAX_SEQ && cursor < counter) {
            LocalSequence.SequenceEntity newSequenceEntity = new LocalSequence.SequenceEntity(currentTime, new Long(cursor).intValue());
            LocalSequence.addSeqEntity(newSequenceEntity);
            cursor++;
        }
        sequenceEntity = LocalSequence.getSeqEntity();
        if (sequenceEntity == null) {
            return null;
        }
        Long uniqueId = SnowFlakeIdGenerator.getUniqueId(sequenceEntity.getCurrentTime(), workerId.intValue(), sequenceEntity.getSeq());
        return uniqueId;
    }

}

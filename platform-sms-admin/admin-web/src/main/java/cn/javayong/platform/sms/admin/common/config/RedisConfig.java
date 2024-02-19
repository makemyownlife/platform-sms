package cn.javayong.platform.sms.admin.common.config;

import cn.javayong.platform.sms.admin.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    public final static String APPINFO_CHANGE_CHANNEL_TOPIC = "appInfoChangeChannelTopic";

    @Autowired
    private AppInfoService appInfoService;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public MessageListener messageListener() {
        MessageListener messageListener = new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] body) {
                String topic = new String(body);
                String content = new String(message.getBody());
                if (APPINFO_CHANGE_CHANNEL_TOPIC.equals(topic)) {
                    appInfoService.reloadAppInfoLocalCache(content);
                }
            }
        };
        return messageListener;
    }

    @Bean
    RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, MessageListener messageListener) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        // 订阅topic - subscribe
        redisMessageListenerContainer.addMessageListener(messageListener, new ChannelTopic(APPINFO_CHANGE_CHANNEL_TOPIC));
        return redisMessageListenerContainer;
    }


}

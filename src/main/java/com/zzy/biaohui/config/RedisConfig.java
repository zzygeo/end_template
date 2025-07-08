package com.zzy.biaohui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author zzy
 * @Description 自定义redis的序列化器
 */

@Configuration
public class RedisConfig {
//    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 设置 Key 序列化方式
        template.setKeySerializer(new StringRedisSerializer());

        // 设置 Value 序列化方式
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 开启事务支持
        template.setEnableTransactionSupport(true);

        // 设置 Hash 的 Key 和 Value 序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}

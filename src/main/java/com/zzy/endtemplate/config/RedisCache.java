package com.zzy.endtemplate.config;
/**
 * @Author zzy
 * @Description redis接口封装
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache {
    @Autowired
    public RedisTemplate redisTemplate;

    /**
     * 获取缓存的基本对象
     * @param key 键
     * @return 数据
     */
    public <T> T getCacheObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * 缓存基本对象 Integer、String、实体类等
     * @param key 键
     * @param value 值
     * @param timeout 时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 删除单个对象
     * @param key
     * @return
     */
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 判断 key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public Boolean hasKey(String key)
    {
        return redisTemplate.hasKey(key);
    }

    /**
     * 增加key的值并设置过期时间
     * @param key
     * @param time
     * @param timeUnit
     * @return
     */
    public Long incrAndExpire(String key, Long time, TimeUnit timeUnit) {
        return (Long) redisTemplate.execute(new SessionCallback<Long>() {
            @Override
            public <K, V> Long execute(RedisOperations<K, V> operations) throws DataAccessException {
                ValueOperations<K, V> valueOps = operations.opsForValue();
                operations.multi(); // 开始事务
                valueOps.increment((K) key); // 执行递增操作
                operations.expire((K) key, time, timeUnit); // 设置过期时间
                List<Object> results = operations.exec(); // 提交事务
                return (Long) results.get(0); // 返回递增后的新值
            }
        });
    }
}

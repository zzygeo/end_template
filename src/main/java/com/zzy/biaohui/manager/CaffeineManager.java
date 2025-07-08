package com.zzy.biaohui.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@Component
public class CaffeineManager {
    private final Cache<String, Object> LOCAL_CACHE = Caffeine.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES)
            .initialCapacity(1024).maximumSize(10000).build();

    /**
     * 获取
     * @param key
     * @return
     */
    public Object get(String key) {
        return LOCAL_CACHE.getIfPresent(key);
    }

    /**
     * 存放
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        LOCAL_CACHE.put(key, value);
    }

    /**
     * 移除
     * @param key
     */
    public void remove(String key) {
        LOCAL_CACHE.invalidate(key);
    }


}

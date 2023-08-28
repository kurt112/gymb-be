package com.kurt.gym.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
public class CachingConfig {

    @Autowired
    CacheManager cacheManager;

    public void evictAllCaches() {
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> {
                    Cache currentCache = cacheManager.getCache(cacheName);

                    if (currentCache != null) {
                        currentCache.clear();
                    }

                });
    }

    // 1 hour
    @Scheduled(fixedRate = 3600000)
    public void evictAllcachesAtIntervals() {
        evictAllCaches();
    }

}
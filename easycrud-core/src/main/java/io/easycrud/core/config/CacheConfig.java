package io.easycrud.core.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

//@Configuration
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(name = "custom.cache.engine", havingValue = "Caffeine")
    public Cache<String, Object> caffeine() {
        return Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
//                .expireAfterWrite(60, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000)
                .build();
    }

}
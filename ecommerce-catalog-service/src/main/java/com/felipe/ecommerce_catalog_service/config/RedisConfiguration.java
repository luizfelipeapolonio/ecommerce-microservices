package com.felipe.ecommerce_catalog_service.config;

import com.felipe.ecommerce_catalog_service.dtos.HomepageProductsDTO;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.Duration;

import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@Configuration
@EnableCaching
public class RedisConfiguration {
  private static final Duration CACHE_TTL = Duration.ofHours(1L);

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .entryTtl(CACHE_TTL)
      .disableCachingNullValues()
      .serializeValuesWith(fromSerializer(new Jackson2JsonRedisSerializer<>(HomepageProductsDTO.class)));

    return RedisCacheManager
      .builder(redisConnectionFactory)
      .cacheDefaults(redisCacheConfiguration)
      .build();
  }

  @Bean
  public RedisTemplate<String, HomepageProductsDTO> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, HomepageProductsDTO> template = new RedisTemplate<>();
    template.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(HomepageProductsDTO.class));
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}

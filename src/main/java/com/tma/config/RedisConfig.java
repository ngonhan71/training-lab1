package com.tma.config;

import com.tma.controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.commandTimeout}")
    private int commandTimeout;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
       try {
           RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
           config.setPassword(RedisPassword.none());

           LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                   .commandTimeout(Duration.ofSeconds(commandTimeout)).build();
           return new LettuceConnectionFactory(config, clientConfiguration);
       } catch (RedisConnectionFailureException ex) {
            logger.error("Redis Error: " + ex.getMessage());
            return null;
        }
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        RedisConnectionFactory factory = redisConnectionFactory();
        if (factory != null) {
            template.setConnectionFactory(factory);
            template.setKeySerializer(new StringRedisSerializer());
        }
        return template;
    }

}

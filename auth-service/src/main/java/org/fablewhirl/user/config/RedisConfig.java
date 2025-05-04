package org.fablewhirl.user.config;

import org.keycloak.representations.AccessTokenResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, AccessTokenResponse> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, AccessTokenResponse> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(AccessTokenResponse.class));
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
}

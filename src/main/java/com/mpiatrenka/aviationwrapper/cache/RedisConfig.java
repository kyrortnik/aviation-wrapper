package com.mpiatrenka.aviationwrapper.cache;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.List;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, List<AirportDetails>> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, List<AirportDetails>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, AirportDetails.class);

        Jackson2JsonRedisSerializer<List<AirportDetails>> valueSerializer =
                new Jackson2JsonRedisSerializer<>(mapper, type);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}

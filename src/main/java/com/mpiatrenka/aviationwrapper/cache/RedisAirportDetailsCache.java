package com.mpiatrenka.aviationwrapper.cache;

import com.mpiatrenka.aviationwrapper.dto.AirportDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisAirportDetailsCache implements AirportDetailsCache {

    private final RedisTemplate<String, List<AirportDetails>> redisTemplate;

    @Override
    public Optional<List<AirportDetails>> getAirportDetails(String icaoId) {
        return Optional.ofNullable(redisTemplate.opsForValue()
                .get("airport:" + icaoId));
    }

    @Override
    public void saveDetails(String icaoId, List<AirportDetails> details) {
        redisTemplate.opsForValue().set("airport:" + icaoId, details);
    }

}

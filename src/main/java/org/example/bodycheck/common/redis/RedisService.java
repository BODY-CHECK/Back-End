package org.example.bodycheck.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public String getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void saveKeyValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Transactional
    public void saveKeyValueWithTTL(String key, String value, long ttl) {
        redisTemplate.opsForValue().set(key, value, Duration.ofMillis(ttl));
    }

    @Transactional
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }
}

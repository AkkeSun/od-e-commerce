package com.sweettracker.account.account.adapter.out.redis;

import com.sweettracker.account.account.application.port.out.RegisterTokenCachePort;
import com.sweettracker.account.account.domain.TokenCache;
import com.sweettracker.account.global.util.JsonUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPersistenceAdapter implements RegisterTokenCachePort {

    private final JsonUtil jsonUtil;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${jwt.token.refresh-valid-time}")
    private long validTime;
    private String tokenCacheKey = "%s-%s::token";

    @CircuitBreaker(name = "redis", fallbackMethod = "registerTokenFallback")
    public void registerToken(TokenCache token) {
        String key = String.format(tokenCacheKey, token.getEmail(), token.getUserAgent());
        String redisData = jsonUtil.toJsonString(token);
        redisTemplate.opsForValue().set(key, redisData, validTime, TimeUnit.MILLISECONDS);
    }

    private void registerTokenFallback(TokenCache token, Exception e) {
        log.error("[registerTokenFallback] call - " + e.getMessage());
    }
}

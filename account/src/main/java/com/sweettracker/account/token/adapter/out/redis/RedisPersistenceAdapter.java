package com.sweettracker.account.token.adapter.out.redis;

import com.sweettracker.account.global.util.JsonUtil;
import com.sweettracker.account.token.application.port.out.FindTokenCachePort;
import com.sweettracker.account.token.application.port.out.RegisterTokenCachePort;
import com.sweettracker.account.token.domain.Token;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPersistenceAdapter implements RegisterTokenCachePort, FindTokenCachePort {

    private final JsonUtil jsonUtil;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${jwt.token.refresh-valid-time}")
    private long validTime;
    private String tokenCacheKey = "%s-%s::token";

    @CircuitBreaker(name = "redis", fallbackMethod = "registerTokenFallback")
    public void registerToken(Token token) {
        String key = String.format(tokenCacheKey, token.getEmail(), token.getUserAgent());
        String redisData = jsonUtil.toJsonString(token);
        redisTemplate.opsForValue().set(key, redisData, validTime, TimeUnit.MILLISECONDS);
    }

    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findByEmailAndUserAgentFallback")
    public Token findByEmailAndUserAgent(String email, String userAgent) {
        String key = String.format(tokenCacheKey, email, userAgent);
        String redisData = redisTemplate.opsForValue().get(key);
        if (!StringUtils.hasText(redisData)) {
            return null;
        }
        return jsonUtil.parseJson(redisData, Token.class);
    }

    private void registerTokenFallback(Token token, Exception e) {
        log.error("[registerTokenFallback] call - " + e.getMessage());
    }

    private Token findByEmailAndUserAgentFallback(String email, String userAgent,
        Exception e) {
        log.error("[findByEmailAndUserAgentFallback] call - " + e.getMessage());
        return null;
    }
}

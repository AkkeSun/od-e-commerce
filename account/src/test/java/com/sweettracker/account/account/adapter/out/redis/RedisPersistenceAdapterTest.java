package com.sweettracker.account.account.adapter.out.redis;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.account.domain.TokenCache;
import com.sweettracker.account.global.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class RedisPersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    JsonUtil jsonUtil;
    @Autowired
    private RedisPersistenceAdapter redisPersistenceAdapter;

    @BeforeEach
    void init() {
        circuitBreakerRegistry.circuitBreaker("redis").reset();
    }

    @Nested
    @DisplayName("[registerToken] 토큰을 등록하는 메소드")
    class Describe_registerToken {

        @Test
        @DisplayName("[success] 토큰이 정상적으로 등록되는지 확인한다.")
        void success() {
            // given
            TokenCache token = TokenCache.builder()
                .email("od@gmail.com")
                .userAgent("chrome")
                .refreshToken("test refresh token")
                .regDateTime("2021-08-01 00:00:00")
                .build();

            // when
            redisPersistenceAdapter.registerToken(token);
            String key = String.format("%s-%s::token", token.getEmail(), token.getUserAgent());
            String redisData = redisTemplate.opsForValue().get(key);
            TokenCache result = jsonUtil.parseJson(redisData, TokenCache.class);

            // then
            assertThat(result.getEmail()).isEqualTo(token.getEmail());
            assertThat(result.getUserAgent()).isEqualTo(token.getUserAgent());
            assertThat(result.getRefreshToken()).isEqualTo(token.getRefreshToken());
            assertThat(result.getRegDateTime()).isEqualTo(token.getRegDateTime());
            redisTemplate.delete(key);
        }

        @Test
        @DisplayName("[error] 토큰 저장중 오류가 발생할 경우 서킷브레이커가 오픈된다.")
        void error() {
            // given
            TokenCache token = null;

            // when
            redisPersistenceAdapter.registerToken(token);

            // then
            assertThat(circuitBreakerRegistry.circuitBreaker("redis")
                .getMetrics().getNumberOfFailedCalls()).isEqualTo(1);
            assertThat(circuitBreakerRegistry.circuitBreaker("redis")
                .getState().toString()).isEqualTo("OPEN");
        }
    }
}
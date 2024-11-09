package com.sweettracker.account.token.application.service.register_token_by_refresh;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.global.exception.CustomAuthenticationException;
import com.sweettracker.account.global.exception.ErrorCode;
import com.sweettracker.account.token.application.port.out.DeleteTokenPort;
import com.sweettracker.account.token.application.port.out.FindTokenCachePort;
import com.sweettracker.account.token.application.port.out.FindTokenPort;
import com.sweettracker.account.token.application.port.out.RegisterTokenCachePort;
import com.sweettracker.account.token.application.port.out.RegisterTokenPort;
import com.sweettracker.account.token.domain.Token;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class RegisterTokenByRefreshServiceTest extends IntegrationTestSupport {

    @Autowired
    RegisterTokenByRefreshService service;

    @Autowired
    RegisterTokenPort registerTokenPort;

    @Autowired
    RegisterTokenCachePort registerTokenCachePort;

    @Autowired
    FindTokenCachePort findTokenCachePort;

    @Autowired
    DeleteTokenPort deleteTokenPort;

    @Autowired
    FindTokenPort findTokenPort;

    @Nested
    @DisplayName("[registerTokenByRefresh] 리프레시 토큰으로 사용자 토큰을 갱신하는 메소드")
    class Describe_registerTokenByRefresh {

        @Test
        @DisplayName("[success] 입력받은 토큰이 레디스에 저장되어 있고 유효하다면 토큰을 갱신하고 응답한다.")
        void success1() {
            // given
            circuitBreakerRegistry.circuitBreaker("redis").reset();
            Token token = Token.builder()
                .email("od@registerTokenByRefresh.success")
                .refreshToken(jwtUtil.createRefreshToken("od@registerTokenByRefresh.success"))
                .userAgent("chrome")
                .regDateTime(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role("ROLE_CUSTOMER")
                .build();
            registerTokenPort.registerToken(token);
            registerTokenCachePort.registerToken(token);
            given(userAgentUtil.getUserAgent()).willReturn("chrome");

            // when
            RegisterTokenByRefreshServiceResponse serviceResponse = service
                .registerTokenByRefresh(token.getRefreshToken());
            Token redisToken = findTokenCachePort
                .findByEmailAndUserAgent(token.getEmail(), token.getUserAgent());
            Token dbToken = findTokenPort
                .findByEmailAndUserAgent(token.getEmail(), token.getUserAgent());

            // then
            assertThat(redisToken.getRefreshToken()).isEqualTo(serviceResponse.refreshToken());
            assertThat(redisToken.getEmail()).isEqualTo(token.getEmail());
            assertThat(redisToken.getUserAgent()).isEqualTo(token.getUserAgent());
            assertThat(redisToken.getRole()).isEqualTo(token.getRole());
            assertThat(redisToken.getRegDateTime()).isNotEqualTo(token.getRegDateTime());
            assertThat(dbToken.getRefreshToken()).isEqualTo(serviceResponse.refreshToken());
            assertThat(dbToken.getEmail()).isEqualTo(token.getEmail());
            assertThat(dbToken.getUserAgent()).isEqualTo(token.getUserAgent());
            assertThat(dbToken.getRole()).isEqualTo(token.getRole());
            assertThat(dbToken.getRegDateTime()).isNotEqualTo(token.getRegDateTime());
            deleteTokenPort.deleteByEmail(token.getEmail());
            redisTemplate.delete(token.getEmail() + "-" + token.getUserAgent() + "::token");
        }

        @Test
        @DisplayName("[success] 레디스 서킷브레이커가 오픈된 경우 DB 에서 토큰을 조회하고 조회된 토큰이 유효하다면 토큰을 갱신하고 응답한다.")
        void success2(CapturedOutput output) {
            // given
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("redis");
            try {
                throw new RuntimeException("test exception");
            } catch (Exception e) {
                circuitBreaker.onError(1, TimeUnit.DAYS, e);
            }

            Token token = Token.builder()
                .email("od@test3.com")
                .refreshToken(jwtUtil.createRefreshToken("od@test3.com"))
                .userAgent("chrome")
                .regDateTime(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role("ROLE_CUSTOMER")
                .build();
            registerTokenPort.registerToken(token);
            given(userAgentUtil.getUserAgent()).willReturn("chrome");

            // when
            RegisterTokenByRefreshServiceResponse serviceResponse = service
                .registerTokenByRefresh(token.getRefreshToken());
            Token dbToken = findTokenPort
                .findByEmailAndUserAgent(token.getEmail(), token.getUserAgent());

            // then
            assertThat(circuitBreaker.getState().name()).isEqualTo("OPEN");
            assertThat(dbToken.getRefreshToken()).isEqualTo(serviceResponse.refreshToken());
            assertThat(dbToken.getEmail()).isEqualTo(token.getEmail());
            assertThat(dbToken.getUserAgent()).isEqualTo(token.getUserAgent());
            assertThat(dbToken.getRole()).isEqualTo(token.getRole());
            assertThat(dbToken.getRegDateTime()).isNotEqualTo(token.getRegDateTime());
            assertThat(output).contains("[Token cache notfound]");
            deleteTokenPort.deleteByEmail(token.getEmail());
            circuitBreaker.reset();
        }

        @Test
        @DisplayName("[error] 입력받은 토큰이 유효하지 않다면 CustomAuthenticationException 을 응답한다.")
        void error1(CapturedOutput output) {
            // given
            circuitBreakerRegistry.circuitBreaker("redis").reset();
            String refreshToken = "invalid-refresh-token";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () ->
                    service.registerTokenByRefresh(refreshToken));

            // then
            assertThat(output).doesNotContain("[Token cache notfound]");
            assertThat(output).doesNotContain("[Invalid token]");
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[error] 입력받은 토큰의 유효기간이 만료되었다면 않다면 CustomAuthenticationException 을 응답한다.")
        void error2(CapturedOutput output) throws InterruptedException {
            // given
            circuitBreakerRegistry.circuitBreaker("redis").reset();
            String refreshToken = jwtUtil.createRefreshToken("helloJwt");
            Thread.sleep(12000);

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () ->
                    service.registerTokenByRefresh(refreshToken));

            // then
            assertThat(output).doesNotContain("[Token cache notfound]");
            assertThat(output).doesNotContain("[Invalid token]");
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[error] 입력받은 토큰이 레디스와 DB에 저장되어 있지 않다면 CustomAuthenticationException 을 응답한다.")
        void error3(CapturedOutput output) {
            // given
            circuitBreakerRegistry.circuitBreaker("redis").reset();
            String refreshToken = jwtUtil.createRefreshToken("helloJwt");

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () ->
                    service.registerTokenByRefresh(refreshToken));

            // then
            assertThat(output).contains("[Token cache notfound]");
            assertThat(output).contains("[Invalid token]");
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("[error] 입력받은 토큰이 저장된 리프레시 토큰과 다르다면 CustomAuthenticationException 을 응답한다.")
        void error4(CapturedOutput output) {
            // given
            circuitBreakerRegistry.circuitBreaker("redis").reset();
            Token token = Token.builder()
                .email("helloJwt")
                .refreshToken(jwtUtil.createRefreshToken("anotherToken"))
                .userAgent("chrome")
                .regDateTime(LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .role("ROLE_CUSTOMER")
                .build();
            registerTokenCachePort.registerToken(token);
            given(userAgentUtil.getUserAgent()).willReturn("chrome");
            String refreshToken = jwtUtil.createRefreshToken("helloJwt");

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () ->
                    service.registerTokenByRefresh(refreshToken));

            // then
            assertThat(output).doesNotContain("[Token cache notfound]");
            assertThat(output).contains("[Invalid token]");
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_REFRESH_TOKEN);
            deleteTokenPort.deleteByEmail(token.getEmail());
        }
    }
}
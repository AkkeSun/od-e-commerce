package com.sweettracker.account.account.adapter.out.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.account.domain.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TokenPersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    TokenPersistenceAdapter tokenPersistenceAdapter;

    @Nested
    @DisplayName("[findByEmailAndUserAgent] 사용자 이메일과 UserAgent 로 토큰을 조회하는 메소드")
    class Describe_findByEmailAndUserAgent {

        @Test
        @DisplayName("[success] 조회된 토큰이 있다면 토큰 정보를 응답하는지 확인한다.")
        void success1() {
            // given
            Token token = Token.builder()
                .email("findByEmailAndUserAgent.success1")
                .userAgent("findByEmailAndUserAgent.success1")
                .refreshToken("findByEmailAndUserAgent.success1")
                .regDateTime("findByEmailAndUserAgent.success1")
                .role("ROLE_CUSTOMER")
                .build();
            tokenPersistenceAdapter.registerToken(token);

            // when
            Token result = tokenPersistenceAdapter
                .findByEmailAndUserAgent(token.getEmail(), token.getUserAgent());

            // then
            assertThat(result.getEmail()).isEqualTo(token.getEmail());
            assertThat(result.getUserAgent()).isEqualTo(token.getUserAgent());
            assertThat(result.getRefreshToken()).isEqualTo(token.getRefreshToken());
            assertThat(result.getRegDateTime()).isEqualTo(token.getRegDateTime());
            tokenPersistenceAdapter.deleteByEmail(token.getEmail());
        }

        @Test
        @DisplayName("[success] 조회된 토큰이 없다면 null 을 응답한다.")
        void success2() {
            // when
            Token result = tokenPersistenceAdapter
                .findByEmailAndUserAgent("unKnown", "unKnown");

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("[registerToken] 토큰을 등록하는 메소드")
    class Describe_registerToken {

        @Test
        @DisplayName("[success] 저장된 토큰이 없다면 새로운 토큰을 등록하는지 확인한다")
        void success1() {
            // given
            Token token = Token.builder()
                .email("registerToken.success1")
                .userAgent("registerToken.success1")
                .refreshToken("registerToken.success1")
                .regDateTime("registerToken.success1")
                .role("ROLE_CUSTOMER")
                .build();
            Token token1 = tokenPersistenceAdapter
                .findByEmailAndUserAgent(token.getEmail(), token.getUserAgent());

            // when
            tokenPersistenceAdapter.registerToken(token);
            Token token2 = tokenPersistenceAdapter
                .findByEmailAndUserAgent(token.getEmail(), token.getUserAgent());

            // then
            assertThat(token1).isNull();
            assertThat(token2.getEmail()).isEqualTo(token.getEmail());
            assertThat(token2.getUserAgent()).isEqualTo(token.getUserAgent());
            assertThat(token2.getRefreshToken()).isEqualTo(token.getRefreshToken());
            assertThat(token2.getRegDateTime()).isEqualTo(token.getRegDateTime());
            tokenPersistenceAdapter.deleteByEmail(token.getEmail());
        }

        @Test
        @DisplayName("[success] 저장된 토큰이 있다면 토큰 정보를 업데이트하는지 확인한다")
        void success2() {
            // given
            Token token1Domain = Token.builder()
                .email("registerToken.success2")
                .userAgent("registerToken.success2")
                .refreshToken("registerToken.success2")
                .regDateTime("registerToken.success2")
                .role("ROLE_CUSTOMER")
                .build();
            tokenPersistenceAdapter.registerToken(token1Domain);
            Token token1Domain2 = Token.builder()
                .email("registerToken.success2")
                .userAgent("registerToken.success2.changed")
                .refreshToken("registerToken.success2.changed")
                .regDateTime("registerToken.success2.changed")
                .build();

            // when
            tokenPersistenceAdapter.registerToken(token1Domain2);
            Token token1 = tokenPersistenceAdapter
                .findByEmailAndUserAgent(token1Domain.getEmail(), token1Domain.getUserAgent());
            Token token2 = tokenPersistenceAdapter
                .findByEmailAndUserAgent(token1Domain2.getEmail(), token1Domain2.getUserAgent());

            // then
            assertThat(token1).isNull();
            assertThat(token2.getEmail()).isEqualTo(token1Domain2.getEmail());
            assertThat(token2.getUserAgent()).isEqualTo(token1Domain2.getUserAgent());
            assertThat(token2.getRefreshToken()).isEqualTo(token1Domain2.getRefreshToken());
            assertThat(token2.getRegDateTime()).isEqualTo(token1Domain2.getRegDateTime());
            tokenPersistenceAdapter.deleteByEmail(token1Domain2.getEmail());
        }
    }
}
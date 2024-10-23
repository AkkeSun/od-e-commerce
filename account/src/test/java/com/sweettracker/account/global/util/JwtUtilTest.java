package com.sweettracker.account.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class JwtUtilTest extends IntegrationTestSupport {

    @Autowired
    JwtUtil jwtUtil;

    @Nested
    @DisplayName("[createAccessToken] 사용자 정보를 기반으로 인증 토큰을 생성하여 응답하는 메소드")
    class Describe_createAccessToken {

        @Test
        @DisplayName("[success] 정상적으로 인증 토큰이 생성되는지 확인한다.")
        void success() {
            // given
            Account account = Account.builder()
                .id(1L)
                .email("od@gmail.com")
                .role(Role.ROLE_SELLER)
                .build();

            // when
            String accessToken = jwtUtil.createAccessToken(account);
            String email = jwtUtil.getEmail(accessToken);
            boolean exceptExpiration = jwtUtil.validateTokenExceptExpiration(accessToken);

            // then
            assertThat(email).isEqualTo(account.getEmail());
            assertThat(exceptExpiration).isTrue();
        }
    }

    @Nested
    @DisplayName("[createRefreshToken] 사용자 이메일을 기반으로 리프레시 토큰을 생성하여 응답하는 메소드")
    class Describe_createRefreshToken {

        @Test
        @DisplayName("[success] 정상적으로 리프레시 토큰이 생성되는지 확인한다.")
        void success() {
            // given
            String email = "od@test.gmail.com";

            // when
            String refreshToken = jwtUtil.createRefreshToken(email);
            String emailFromToken = jwtUtil.getEmail(refreshToken);

            // then
            assertThat(emailFromToken).isEqualTo(email);
        }
    }
}
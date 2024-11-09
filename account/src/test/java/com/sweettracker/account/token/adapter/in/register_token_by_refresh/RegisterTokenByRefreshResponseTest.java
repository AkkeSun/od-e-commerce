package com.sweettracker.account.token.adapter.in.register_token_by_refresh;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sweettracker.account.token.application.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterTokenByRefreshResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            RegisterTokenByRefreshServiceResponse serviceResponse = RegisterTokenByRefreshServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            // when
            RegisterTokenByRefreshResponse response =
                new RegisterTokenByRefreshResponse().of(serviceResponse);

            // then
            assertEquals(response.getAccessToken(), serviceResponse.accessToken());
            assertEquals(response.getRefreshToken(), serviceResponse.refreshToken());
        }
    }
}
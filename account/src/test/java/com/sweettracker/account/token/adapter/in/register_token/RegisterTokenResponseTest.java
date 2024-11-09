package com.sweettracker.account.token.adapter.in.register_token;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sweettracker.account.token.application.service.register_token.RegisterTokenServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterTokenResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            RegisterTokenServiceResponse serviceResponse = RegisterTokenServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            // when
            RegisterTokenResponse response = new RegisterTokenResponse().of(serviceResponse);

            // then
            assertEquals(response.getAccessToken(), serviceResponse.accessToken());
            assertEquals(response.getRefreshToken(), serviceResponse.refreshToken());
        }
    }
}
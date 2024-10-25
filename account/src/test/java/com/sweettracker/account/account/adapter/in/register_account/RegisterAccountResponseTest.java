package com.sweettracker.account.account.adapter.in.register_account;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sweettracker.account.account.application.service.register_account.RegisterAccountServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterAccountResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            RegisterAccountServiceResponse serviceResponse = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

            // when
            RegisterAccountResponse response = new RegisterAccountResponse().of(
                serviceResponse);

            // then
            assertEquals(response.getAccessToken(), serviceResponse.accessToken());
            assertEquals(response.getRefreshToken(), serviceResponse.refreshToken());
        }
    }
}
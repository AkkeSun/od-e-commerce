package com.sweettracker.account.account.adapter.in.find_account;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.account.application.service.find_account.FindAccountServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindAccountInfoResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객채를 API 응답 객채로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            FindAccountServiceResponse serviceResponse = FindAccountServiceResponse.builder()
                .id(1L)
                .username("od")
                .userTel("01012341234")
                .address("서울특별시 송파구")
                .email("test@gmail.com")
                .role("ROLE_CUSTOMER")
                .build();

            // when
            FindAccountResponse response = new FindAccountResponse().of(serviceResponse);

            // then
            assertThat(response.getId()).isEqualTo(serviceResponse.id());
            assertThat(response.getUsername()).isEqualTo(serviceResponse.username());
            assertThat(response.getUserTel()).isEqualTo(serviceResponse.userTel());
            assertThat(response.getAddress()).isEqualTo(serviceResponse.address());
            assertThat(response.getEmail()).isEqualTo(serviceResponse.email());
            assertThat(response.getRole()).isEqualTo(serviceResponse.role());
        }
    }
}
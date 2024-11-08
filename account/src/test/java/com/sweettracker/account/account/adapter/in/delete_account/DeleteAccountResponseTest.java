package com.sweettracker.account.account.adapter.in.delete_account;

import com.sweettracker.account.account.application.service.delete_account.DeleteAccountServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeleteAccountResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체를 API 응답 객체로 잘 변환하는지 확인한다")
        void success() {
            // given
            DeleteAccountServiceResponse serviceResponse = DeleteAccountServiceResponse.builder()
                .id(10L)
                .email("od@test.com")
                .result("Y")
                .build();

            // when
            DeleteAccountResponse response = new DeleteAccountResponse().of(serviceResponse);

            // then
            assert response.getId().equals(serviceResponse.id());
            assert response.getEmail().equals(serviceResponse.email());
            assert response.getResult().equals(serviceResponse.result());
        }
    }
}
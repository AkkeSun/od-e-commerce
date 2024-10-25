package com.sweettracker.account.account.adapter.in.register_account;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.account.application.port.in.command.RegisterAccountCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterAccountRequestTest {

    @Nested
    @DisplayName("[toCommand] API 요청객채를 command 로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] API 요청객채가 command 로 잘 변환하는지 확인한다")
        void success() {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("od@test.com")
                .password("1234")
                .passwordCheck("1234")
                .role("ROLE_CUSTOMER")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();

            // when
            RegisterAccountCommand command = request.toCommand();

            // then
            assertThat(command.email()).isEqualTo(request.getEmail());
            assertThat(command.password()).isEqualTo(request.getPassword());
            assertThat(command.role()).isEqualTo(request.getRole());
            assertThat(command.username()).isEqualTo(request.getUsername());
            assertThat(command.userTel()).isEqualTo(request.getUserTel());
            assertThat(command.address()).isEqualTo(request.getAddress());
        }
    }
}
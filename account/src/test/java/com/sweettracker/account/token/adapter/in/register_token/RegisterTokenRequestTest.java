package com.sweettracker.account.token.adapter.in.register_token;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.token.application.port.in.command.RegisterTokenCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterTokenRequestTest {

    @Nested
    @DisplayName("[toCommand] API 요청객채를 command 로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] API 요청객채가 command 로 잘 변환하는지 확인한다")
        void success() {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

            // when
            RegisterTokenCommand command = request.toCommand();

            // then
            assertThat(command.email()).isEqualTo(request.getEmail());
            assertThat(command.password()).isEqualTo(request.getPassword());
        }
    }
}
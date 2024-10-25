package com.sweettracker.account.account.adapter.in.update_account;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.account.application.port.in.command.UpdateAccountCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateAccountRequestTest {

    @Nested
    @DisplayName("[toCommand] API 요청객채를 command 로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] API 요청객채가 command 로 잘 변환하는지 확인한다")
        void success() {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("01012341234")
                .address("서울특별시 송파구")
                .build();
            String accessToken = "test-access-token";

            // when
            UpdateAccountCommand command = request.toCommand(accessToken);

            // then
            assertThat(command.password()).isEqualTo(request.getPassword());
            assertThat(command.passwordCheck()).isEqualTo(request.getPasswordCheck());
            assertThat(command.username()).isEqualTo(request.getUsername());
            assertThat(command.userTel()).isEqualTo(request.getUserTel());
            assertThat(command.address()).isEqualTo(request.getAddress());
            assertThat(command.accessToken()).isEqualTo(accessToken);
        }
    }
}
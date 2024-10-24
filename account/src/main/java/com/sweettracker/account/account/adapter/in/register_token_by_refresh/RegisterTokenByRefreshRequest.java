package com.sweettracker.account.account.adapter.in.register_token_by_refresh;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
class RegisterTokenByRefreshRequest {

    @NotBlank(message = "리프레시 토큰은 필수값 입니다.")
    private String refreshToken;

    @Builder
    public RegisterTokenByRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

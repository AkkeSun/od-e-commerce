package com.sweettracker.account.account.application.service.register_token;

import lombok.Builder;

@Builder
public record RegisterTokenServiceResponse(
    String accessToken, String refreshToken
) {

}

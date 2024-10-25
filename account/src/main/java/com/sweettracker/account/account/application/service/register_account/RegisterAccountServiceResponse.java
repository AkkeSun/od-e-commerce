package com.sweettracker.account.account.application.service.register_account;

import lombok.Builder;

@Builder
public record RegisterAccountServiceResponse(
    String accessToken, String refreshToken
) {

}

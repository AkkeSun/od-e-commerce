package com.sweettracker.account.account.application.service.find_account_info;

import lombok.Builder;

@Builder
public record FindAccountServiceResponse(
    Long id,
    String email,
    String username,
    String userTel,
    String address,
    String role
) {

}

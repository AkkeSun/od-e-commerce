package com.sweettracker.account.account.application.port.in.command;

import lombok.Builder;

@Builder
public record UpdateAccountCommand(
    String accessToken,
    String password,
    String passwordCheck,
    String username,
    String userTel,
    String address) {

}

package com.sweettracker.account.account.application.port.in.command;

import lombok.Builder;

@Builder
public record RegisterTokenCommand(
    String email, String password
) {

}

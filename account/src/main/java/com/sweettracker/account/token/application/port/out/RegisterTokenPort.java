package com.sweettracker.account.token.application.port.out;

import com.sweettracker.account.token.domain.Token;

public interface RegisterTokenPort {

    void registerToken(Token token);
}

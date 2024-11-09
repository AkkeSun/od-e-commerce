package com.sweettracker.account.token.application.port.out;

import com.sweettracker.account.token.domain.Token;

public interface RegisterTokenCachePort {

    void registerToken(Token token);
}

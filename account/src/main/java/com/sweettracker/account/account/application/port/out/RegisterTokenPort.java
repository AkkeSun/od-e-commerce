package com.sweettracker.account.account.application.port.out;

import com.sweettracker.account.account.domain.TokenCache;

public interface RegisterTokenPort {

    void registerToken(TokenCache token);
}

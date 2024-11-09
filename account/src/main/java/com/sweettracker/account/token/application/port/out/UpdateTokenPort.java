package com.sweettracker.account.token.application.port.out;

import com.sweettracker.account.token.domain.Token;

public interface UpdateTokenPort {

    void updateToken(Token tokenCache);
}

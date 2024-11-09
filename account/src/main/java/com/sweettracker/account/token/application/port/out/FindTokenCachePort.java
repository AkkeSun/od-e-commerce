package com.sweettracker.account.token.application.port.out;

import com.sweettracker.account.token.domain.Token;

public interface FindTokenCachePort {

    Token findByEmailAndUserAgent(String email, String userAgent);
}

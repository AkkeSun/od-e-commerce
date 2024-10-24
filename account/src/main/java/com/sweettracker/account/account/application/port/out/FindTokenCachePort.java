package com.sweettracker.account.account.application.port.out;

import com.sweettracker.account.account.domain.Token;

public interface FindTokenCachePort {

    Token findByEmailAndUserAgent(String email, String userAgent);
}

package com.sweettracker.account.account.application.port.out;

import com.sweettracker.account.account.domain.TokenCache;

public interface FindTokenPort {

    TokenCache findByEmailAndUserAgent(String email, String userAgent);
}

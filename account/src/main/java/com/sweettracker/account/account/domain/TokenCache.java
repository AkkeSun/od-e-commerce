package com.sweettracker.account.account.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenCache {

    private Long id;
    private String email;
    private String userAgent;
    private String refreshToken;
    private String regDateTime;

    @Builder
    public TokenCache(Long id, String email, String userAgent, String refreshToken,
        String regDateTime) {
        this.id = id;
        this.email = email;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.regDateTime = regDateTime;
    }
}

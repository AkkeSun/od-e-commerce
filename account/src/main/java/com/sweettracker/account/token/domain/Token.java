package com.sweettracker.account.token.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Token {

    private Long id;
    private String email;
    private String userAgent;
    private String refreshToken;
    private String regDateTime;
    private String role;

    @Builder
    public Token(Long id, String email, String userAgent, String refreshToken,
        String regDateTime,
        String role) {
        this.id = id;
        this.email = email;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.regDateTime = regDateTime;
        this.role = role;
    }

    public boolean isDifferentRefreshToken(String refreshToken) {
        return !this.refreshToken.equals(refreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateRegTime(String regDateTime) {
        this.regDateTime = regDateTime;
    }
}

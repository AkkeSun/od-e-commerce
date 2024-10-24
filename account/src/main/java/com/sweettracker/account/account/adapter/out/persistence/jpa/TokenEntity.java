package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.domain.Token;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "TOKEN")
@NoArgsConstructor
class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_INDEX")
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Column(name = "REG_DATE_TIME")
    private String regDateTime;

    @Column(name = "ROLE")
    private String role;

    @Builder
    public TokenEntity(Long id, String email, String userAgent, String refreshToken,
        String regDateTime, String role) {
        this.id = id;
        this.email = email;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.regDateTime = regDateTime;
        this.role = role;
    }

    void updateByDomain(Token tokenCache) {
        this.refreshToken = tokenCache.getRefreshToken();
        this.regDateTime = tokenCache.getRegDateTime();
        this.userAgent = tokenCache.getUserAgent();
    }
}

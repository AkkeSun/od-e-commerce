package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.domain.TokenCache;
import org.springframework.stereotype.Component;

@Component
class TokenMapper {

    public TokenCache toDomain(TokenEntity entity) {
        return TokenCache.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .userAgent(entity.getUserAgent())
            .refreshToken(entity.getRefreshToken())
            .regDateTime(entity.getRegDateTime())
            .build();
    }

    public TokenEntity toEntity(TokenCache domain) {
        return TokenEntity.builder()
            .id(domain.getId())
            .email(domain.getEmail())
            .userAgent(domain.getUserAgent())
            .refreshToken(domain.getRefreshToken())
            .regDateTime(domain.getRegDateTime())
            .build();
    }
}

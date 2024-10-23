package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.application.port.out.DeleteTokenPort;
import com.sweettracker.account.account.application.port.out.FindTokenPort;
import com.sweettracker.account.account.application.port.out.RegisterTokenPort;
import com.sweettracker.account.account.domain.TokenCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class TokenPersistenceAdapter implements FindTokenPort, RegisterTokenPort, DeleteTokenPort {

    private final TokenMapper tokenCacheMapper;

    private final TokenRepository tokenRepository;

    @Override
    public TokenCache findByEmailAndUserAgent(String email, String userAgent) {
        return tokenRepository.findByEmailAndUserAgent(email, userAgent)
            .map(tokenCacheMapper::toDomain)
            .orElse(null);
    }

    @Override
    @Transactional // for test
    public void registerToken(TokenCache token) {
        TokenEntity entity = tokenRepository.findByEmail(token.getEmail())
            .map(existingEntity -> {
                existingEntity.updateByDomain(token);
                return existingEntity;
            })
            .orElseGet(() -> tokenCacheMapper.toEntity(token));
        tokenRepository.save(entity);
    }

    @Override
    public void deleteByEmail(String email) {
        tokenRepository.findByEmail(email)
            .ifPresent(tokenRepository::delete);
    }
}

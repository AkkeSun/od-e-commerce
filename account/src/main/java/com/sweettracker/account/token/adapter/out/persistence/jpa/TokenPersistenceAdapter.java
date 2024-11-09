package com.sweettracker.account.token.adapter.out.persistence.jpa;

import com.sweettracker.account.token.application.port.out.DeleteTokenPort;
import com.sweettracker.account.token.application.port.out.FindTokenPort;
import com.sweettracker.account.token.application.port.out.RegisterTokenPort;
import com.sweettracker.account.token.application.port.out.UpdateTokenPort;
import com.sweettracker.account.token.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class TokenPersistenceAdapter implements FindTokenPort, RegisterTokenPort, DeleteTokenPort,
    UpdateTokenPort {

    private final TokenMapper tokenCacheMapper;

    private final TokenRepository tokenRepository;

    @Override
    public Token findByEmailAndUserAgent(String email, String userAgent) {
        return tokenRepository.findByEmailAndUserAgent(email, userAgent)
            .map(tokenCacheMapper::toDomain)
            .orElse(null);
    }

    @Override
    @Transactional // for test @Transactional
    public void registerToken(Token token) {
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

    @Override
    @Transactional // for test @Transactional
    public void updateToken(Token tokenCache) {
        tokenRepository.updateToken(tokenCacheMapper.toEntity(tokenCache));
    }
}

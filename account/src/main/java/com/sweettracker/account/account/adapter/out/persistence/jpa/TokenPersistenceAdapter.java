package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.application.port.out.RegisterTokenPort;
import com.sweettracker.account.account.domain.TokenCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class TokenPersistenceAdapter implements RegisterTokenPort {

    private final TokenMapper tokenCacheMapper;

    private final TokenRepository tokenCacheRepository;

    @Override
    public void registerToken(TokenCache token) {
        TokenEntity entity = tokenCacheRepository.findByEmail(token.getEmail())
            .map(existingEntity -> {
                existingEntity.updateByDomain(token);
                return existingEntity;
            })
            .orElseGet(() -> tokenCacheMapper.toEntity(token));
        tokenCacheRepository.save(entity);
    }
}

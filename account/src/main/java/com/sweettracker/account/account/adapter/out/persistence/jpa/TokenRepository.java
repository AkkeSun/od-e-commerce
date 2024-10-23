package com.sweettracker.account.account.adapter.out.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByEmail(String email);

    Optional<TokenEntity> findByEmailAndUserAgent(String email, String userAgent);

    void deleteByEmail(String email);
}

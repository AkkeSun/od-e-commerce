package com.sweettracker.account.account.adapter.out.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByEmail(String email);

    void deleteByEmail(String email);
}

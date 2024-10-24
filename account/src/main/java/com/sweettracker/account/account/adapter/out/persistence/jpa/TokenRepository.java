package com.sweettracker.account.account.adapter.out.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByEmail(String email);

    Optional<TokenEntity> findByEmailAndUserAgent(String email, String userAgent);

    void deleteByEmail(String email);

    @Modifying
    @Query("update TokenEntity t "
        + "set t.refreshToken = :#{#token.refreshToken}, t.regDateTime = :#{#token.regDateTime} "
        + "where t.email = :#{#token.email} "
        + "and t.userAgent = :#{#token.userAgent}")
    void updateToken(@Param("token") TokenEntity token);
}

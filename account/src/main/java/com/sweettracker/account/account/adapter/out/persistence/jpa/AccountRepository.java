package com.sweettracker.account.account.adapter.out.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByEmail(String email);

    Optional<AccountEntity> findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);

    @Modifying
    @Query("update AccountEntity a "
        + "set a.password = :#{#account.password}, "
        + "    a.username = :#{#account.username}, "
        + "    a.userTel = :#{#account.userTel}, "
        + "    a.address = :#{#account.address} "
        + "where a.id = :#{#account.id} ")
    void update(AccountEntity account);
}

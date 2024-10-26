package com.sweettracker.account.account.adapter.out.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity, Long> {

    Optional<AccountHistoryEntity> findByEmail(String email);
}

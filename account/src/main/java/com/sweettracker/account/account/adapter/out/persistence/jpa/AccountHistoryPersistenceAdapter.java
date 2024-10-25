package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.application.port.out.RegisterAccountHistoryPort;
import com.sweettracker.account.account.domain.AccountHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AccountHistoryPersistenceAdapter implements RegisterAccountHistoryPort {

    private final AccountHistoryMapper mapper;
    private final AccountHistoryRepository accountHistoryRepository;

    @Override
    public void registerAccountHistory(AccountHistory accountHistory) {
        accountHistoryRepository.save(mapper.toEntity(accountHistory));
    }
}

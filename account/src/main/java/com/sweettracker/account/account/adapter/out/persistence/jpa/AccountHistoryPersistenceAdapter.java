package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.application.port.out.DeleteAccountHistoryPort;
import com.sweettracker.account.account.application.port.out.FindAccountHistoryPort;
import com.sweettracker.account.account.application.port.out.RegisterAccountHistoryPort;
import com.sweettracker.account.account.domain.AccountHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AccountHistoryPersistenceAdapter implements RegisterAccountHistoryPort,
    FindAccountHistoryPort, DeleteAccountHistoryPort {

    private final AccountHistoryMapper mapper;
    private final AccountHistoryRepository accountHistoryRepository;

    @Override
    public void registerAccountHistory(AccountHistory accountHistory) {
        accountHistoryRepository.save(mapper.toEntity(accountHistory));
    }

    @Override
    public AccountHistory findByEmail(String email) {
        return accountHistoryRepository.findByEmail(email)
            .map(mapper::toDomain)
            .orElse(null);
    }

    @Override
    public void deleteByEmail(String email) {
        accountHistoryRepository.findByEmail(email)
            .ifPresent(accountHistoryRepository::delete);
    }
}

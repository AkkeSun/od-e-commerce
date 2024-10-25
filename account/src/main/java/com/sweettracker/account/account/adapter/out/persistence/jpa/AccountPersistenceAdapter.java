package com.sweettracker.account.account.adapter.out.persistence.jpa;

import static com.sweettracker.account.global.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;

import com.sweettracker.account.account.application.port.out.DeleteAccountPort;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterAccountPort;
import com.sweettracker.account.account.application.port.out.UpdateAccountPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.global.exception.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AccountPersistenceAdapter implements FindAccountPort, RegisterAccountPort, DeleteAccountPort,
    UpdateAccountPort {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    @Override
    public Account findByEmail(String email) {
        AccountEntity entity = accountRepository.findByEmail(email)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));
        return accountMapper.toDomain(entity);
    }

    @Override
    public Account findByEmailAndPassword(String email, String password) {
        AccountEntity entity = accountRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));
        return accountMapper.toDomain(entity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public void register(Account account) {
        AccountEntity entity = accountMapper.toEntity(account);
        accountRepository.save(entity);
    }

    @Override
    public void deleteByEmail(String email) {
        AccountEntity entity = accountRepository.findByEmail(email)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));
        accountRepository.delete(entity);
    }

    @Override
    public void update(Account account) {
        accountRepository.update(accountMapper.toEntity(account));
    }
}

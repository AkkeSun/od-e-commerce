package com.sweettracker.account.account.application.port.out;

import com.sweettracker.account.account.domain.Account;

public interface FindAccountPort {

    Account findByEmail(String email);

    Account findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}

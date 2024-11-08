package com.sweettracker.account.account.application.port.out;

import com.sweettracker.account.account.domain.Account;

public interface RegisterAccountPort {

    Account register(Account account);
}

package com.sweettracker.account.account.application.port.out;

import com.sweettracker.account.account.domain.AccountHistory;

public interface RegisterAccountHistoryPort {

    void registerAccountHistory(AccountHistory accountHistory);
}

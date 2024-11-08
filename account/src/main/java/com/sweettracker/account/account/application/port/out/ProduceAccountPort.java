package com.sweettracker.account.account.application.port.out;

import com.sweettracker.account.account.domain.Account;

public interface ProduceAccountPort {

    void sendAccountDeleteMsgToOrder(Account account);

    void sendAccountDeleteMsgToDelivery(Account account);
}

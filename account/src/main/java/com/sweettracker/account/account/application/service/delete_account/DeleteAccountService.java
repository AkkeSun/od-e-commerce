package com.sweettracker.account.account.application.service.delete_account;

import com.sweettracker.account.account.application.port.in.DeleteAccountUseCase;
import com.sweettracker.account.account.application.port.out.DeleteAccountPort;
import com.sweettracker.account.account.application.port.out.ProduceAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterAccountHistoryPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.AccountHistory;
import com.sweettracker.account.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteAccountService implements DeleteAccountUseCase {

    private final JwtUtil jwtUtil;
    private final DeleteAccountPort deleteAccountPort;
    private final RegisterAccountHistoryPort registerAccountHistoryPort;
    private final ProduceAccountPort produceAccountPort;

    @Override
    public DeleteAccountServiceResponse deleteAccount(String authentication) {
        Claims claims = jwtUtil.getClaims(authentication);
        deleteAccountPort.deleteByEmail(claims.getSubject());

        AccountHistory history = new AccountHistory().of(claims);
        history.updateType("DELETE");
        registerAccountHistoryPort.registerAccountHistory(history);

        // TODO: another microservice delete message
        Account account = new Account().of(claims);
        produceAccountPort.sendAccountDeleteMsgToOrder(account);
        produceAccountPort.sendAccountDeleteMsgToDelivery(account);

        return DeleteAccountServiceResponse.builder()
            .id(history.getAccountId())
            .email(history.getEmail())
            .result("Y")
            .build();
    }
}

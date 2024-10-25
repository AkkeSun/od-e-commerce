package com.sweettracker.account.account.application.service.update_account;

import com.sweettracker.account.account.application.port.in.UpdateAccountUseCase;
import com.sweettracker.account.account.application.port.in.command.UpdateAccountCommand;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterAccountHistoryPort;
import com.sweettracker.account.account.application.port.out.UpdateAccountPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.AccountHistory;
import com.sweettracker.account.global.util.AesUtil;
import com.sweettracker.account.global.util.JwtUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
class UpdateAccountService implements UpdateAccountUseCase {

    private final AesUtil aesUtil;
    private final JwtUtil jwtUtil;
    private final FindAccountPort findAccountPort;
    private final UpdateAccountPort updateAccountPort;
    private final RegisterAccountHistoryPort registerAccountHistoryPort;

    @Override
    public UpdateAccountServiceResponse updateAccount(UpdateAccountCommand command) {
        String email = jwtUtil.getEmail(command.accessToken());
        Account account = findAccountPort.findByEmail(email);

        List<String> updateList = new ArrayList<>();
        if (StringUtils.hasText(command.username()) &&
            !command.username().equals(account.getUsername())) {
            updateList.add("username");
            account.updateUsername(command.username());
        }
        if (StringUtils.hasText(command.password()) &&
            !aesUtil.matches(command.password(), account.getPassword())) {
            updateList.add("password");
            account.updatePassword(aesUtil.encryptText(command.password()));
        }
        if (StringUtils.hasText(command.userTel()) &&
            !command.userTel().equals(account.getUserTel())) {
            updateList.add("userTel");
            account.updateUserTel(command.userTel());
        }
        if (StringUtils.hasText(command.address()) &&
            !command.address().equals(account.getAddress())) {
            updateList.add("address");
            account.updateAddress(command.address());
        }

        if(updateList.isEmpty()) {
            return UpdateAccountServiceResponse.builder()
                .updateYn("N")
                .updateList(updateList)
                .build();
        }

        AccountHistory history = new AccountHistory().of(account);
        history.updateType("UPDATE");
        history.updateDetailInfo(String.join(",", updateList));

        updateAccountPort.update(account);
        registerAccountHistoryPort.registerAccountHistory(history);

        return UpdateAccountServiceResponse.builder()
            .updateYn("Y")
            .updateList(updateList)
            .build();
    }
}

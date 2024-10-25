package com.sweettracker.account.account.application.service.register_account;

import com.sweettracker.account.account.application.port.in.RegisterAccountUseCase;
import com.sweettracker.account.account.application.port.in.command.RegisterAccountCommand;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterTokenCachePort;
import com.sweettracker.account.account.application.port.out.RegisterTokenPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.Role;
import com.sweettracker.account.account.domain.Token;
import com.sweettracker.account.global.exception.CustomBusinessException;
import com.sweettracker.account.global.exception.ErrorCode;
import com.sweettracker.account.global.util.AesUtil;
import com.sweettracker.account.global.util.DateUtil;
import com.sweettracker.account.global.util.JwtUtil;
import com.sweettracker.account.global.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class RegisterAccountService implements RegisterAccountUseCase {

    private final AesUtil aesUtil;
    private final JwtUtil jwtUtil;
    private final UserAgentUtil userAgentUtil;
    private final DateUtil dateUtil;
    private final FindAccountPort findAccountPort;
    private final RegisterAccountPort registerAccountPort;
    private final RegisterTokenPort registerTokenPort;
    private final RegisterTokenCachePort registerTokenCachePort;

    @Override
    public RegisterAccountServiceResponse registerAccount(RegisterAccountCommand command) {
        if (findAccountPort.existsByEmail(command.email())) {
            throw new CustomBusinessException(ErrorCode.Business_SAVED_ACCOUNT_INFO);
        }

        Account account = Account.builder()
            .email(command.email())
            .password(aesUtil.encryptText(command.password()))
            .username(command.username())
            .userTel(command.userTel())
            .address(command.address())
            .role(Role.valueOf(command.role()))
            .build();
        registerAccountPort.register(account);

        String accessToken = jwtUtil.createAccessToken(account);
        String refreshToken = jwtUtil.createRefreshToken(account.getEmail());

        Token token = Token.builder()
            .email(account.getEmail())
            .userAgent(userAgentUtil.getUserAgent())
            .refreshToken(refreshToken)
            .regDateTime(dateUtil.getCurrentDateTime())
            .role(command.role())
            .build();

        registerTokenPort.registerToken(token);
        registerTokenCachePort.registerToken(token);

        return RegisterAccountServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}

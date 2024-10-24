package com.sweettracker.account.account.application.service.register_token;

import com.sweettracker.account.account.application.port.in.RegisterTokenUseCase;
import com.sweettracker.account.account.application.port.in.command.RegisterTokenCommand;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterTokenCachePort;
import com.sweettracker.account.account.application.port.out.RegisterTokenPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.Token;
import com.sweettracker.account.global.util.AesUtil;
import com.sweettracker.account.global.util.DateUtil;
import com.sweettracker.account.global.util.JwtUtil;
import com.sweettracker.account.global.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class RegisterTokenService implements RegisterTokenUseCase {

    private final AesUtil aesUtil;
    private final JwtUtil jwtUtil;
    private final DateUtil dateUtil;
    private final UserAgentUtil userAgentUtil;
    private final FindAccountPort findAccountPort;
    private final RegisterTokenCachePort registerTokenCachePort;
    private final RegisterTokenPort registerTokenPort;

    @Override
    public RegisterTokenServiceResponse registerToken(RegisterTokenCommand command) {
        Account account = findAccountPort
            .findByEmailAndPassword(command.email(), aesUtil.encryptText(command.password()));

        String accessToken = jwtUtil.createAccessToken(account);
        String refreshToken = jwtUtil.createRefreshToken(command.email());
        Token tokenCache = Token.builder()
            .email(account.getEmail())
            .userAgent(userAgentUtil.getUserAgent())
            .refreshToken(refreshToken)
            .regDateTime(dateUtil.getCurrentDateTime())
            .role(account.getRole().name())
            .build();

        registerTokenPort.registerToken(tokenCache);
        registerTokenCachePort.registerToken(tokenCache);

        return RegisterTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}

package com.sweettracker.account.account.application.service.register_token;

import com.sweettracker.account.account.application.port.in.RegisterTokenUseCase;
import com.sweettracker.account.account.application.port.in.command.RegisterTokenCommand;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterTokenCachePort;
import com.sweettracker.account.account.application.port.out.RegisterTokenPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.TokenCache;
import com.sweettracker.account.global.util.AesUtil;
import com.sweettracker.account.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class RegisterTokenService implements RegisterTokenUseCase {

    private final AesUtil aesUtil;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;
    private final FindAccountPort findAccountPort;
    private final RegisterTokenPort registerTokenPort;
    private final RegisterTokenCachePort registerTokenCachePort;

    @Override
    public RegisterTokenServiceResponse registerToken(RegisterTokenCommand command) {
        Account account = findAccountPort
            .findByEmailAndPassword(command.email(), aesUtil.encryptText(command.password()));

        String accessToken = jwtUtil.createAccessToken(account);
        String refreshToken = jwtUtil.createRefreshToken(command.email());
        TokenCache tokenCache = TokenCache.builder()
            .email(account.getEmail())
            .userAgent(request.getHeader("User-Agent"))
            .refreshToken(refreshToken)
            .regDateTime(LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss")))
            .build();

        registerTokenPort.registerToken(tokenCache);
        registerTokenCachePort.registerToken(tokenCache);

        return RegisterTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}

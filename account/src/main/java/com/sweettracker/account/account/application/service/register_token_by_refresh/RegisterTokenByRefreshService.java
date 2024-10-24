package com.sweettracker.account.account.application.service.register_token_by_refresh;

import com.sweettracker.account.account.application.port.in.RegisterTokenByRefreshUseCase;
import com.sweettracker.account.account.application.port.out.FindTokenCachePort;
import com.sweettracker.account.account.application.port.out.FindTokenPort;
import com.sweettracker.account.account.application.port.out.RegisterTokenCachePort;
import com.sweettracker.account.account.application.port.out.UpdateTokenPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.Token;
import com.sweettracker.account.global.exception.CustomAuthenticationException;
import com.sweettracker.account.global.exception.ErrorCode;
import com.sweettracker.account.global.util.DateUtil;
import com.sweettracker.account.global.util.JwtUtil;
import com.sweettracker.account.global.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@Transactional
@RequiredArgsConstructor
class RegisterTokenByRefreshService implements RegisterTokenByRefreshUseCase {
    
    private final JwtUtil jwtUtil;
    private final DateUtil dateUtil;
    private final UserAgentUtil userAgentUtil;
    private final FindTokenCachePort findRedisCachePort;
    private final FindTokenPort findTokenPort;
    private final UpdateTokenPort updateTokenPort;
    private final RegisterTokenCachePort registerTokenCachePort;

    @Override
    public RegisterTokenByRefreshServiceResponse registerTokenByRefresh(String refreshToken) {
        if (!jwtUtil.validateTokenExceptExpiration(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtUtil.getEmail(refreshToken);
        String userAgent = userAgentUtil.getUserAgent();
        Token savedToken = findRedisCachePort.findByEmailAndUserAgent(email, userAgent);

        if (ObjectUtils.isEmpty(savedToken)) {
            savedToken = findTokenPort.findByEmailAndUserAgent(email, userAgent);
        }
        if (ObjectUtils.isEmpty(savedToken) || savedToken.isDifferentRefreshToken(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Account account = new Account().of(savedToken);
        String newAccessToken = jwtUtil.createAccessToken(account);
        String newRefreshToken = jwtUtil.createRefreshToken(email);

        savedToken.updateRefreshToken(newRefreshToken);
        savedToken.updateRegTime(dateUtil.getCurrentDateTime());

        updateTokenPort.updateToken(savedToken);
        registerTokenCachePort.registerToken(savedToken);

        return RegisterTokenByRefreshServiceResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build();
    }
}

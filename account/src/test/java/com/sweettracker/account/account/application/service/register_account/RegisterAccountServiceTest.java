package com.sweettracker.account.account.application.service.register_account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.account.application.port.in.RegisterAccountUseCase;
import com.sweettracker.account.account.application.port.in.command.RegisterAccountCommand;
import com.sweettracker.account.account.application.port.out.DeleteAccountPort;
import com.sweettracker.account.account.application.port.out.DeleteTokenPort;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.FindTokenCachePort;
import com.sweettracker.account.account.application.port.out.FindTokenPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.Token;
import com.sweettracker.account.global.exception.CustomBusinessException;
import com.sweettracker.account.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegisterAccountServiceTest extends IntegrationTestSupport {

    @Autowired
    RegisterAccountUseCase registerAccountUseCase;

    @Autowired
    FindAccountPort findAccountPort;

    @Autowired
    FindTokenPort findTokenPort;

    @Autowired
    FindTokenCachePort findTokenCachePort;

    @Autowired
    DeleteAccountPort deleteAccountPort;

    @Autowired
    DeleteTokenPort deleteTokenPort;

    @BeforeEach
    void setup() {
        circuitBreakerRegistry.circuitBreaker("redis").reset();
    }
    
    @Nested
    @DisplayName("[registerAccount] 사용자 정보를 등록하는 메소드")
    class registerAccount {

        @Test
        @DisplayName("[success] 등록된 사용자 정보가 아니라면 사용자 정보와 토큰을 등록하고 토큰을 응답한다.")
        void success() {
            // given
            RegisterAccountCommand command = RegisterAccountCommand.builder()
                .email("aaa.success")
                .address("registerAccount.success")
                .password("registerAccount.success")
                .userTel("01012341234")
                .role("ROLE_CUSTOMER")
                .build();
            given(userAgentUtil.getUserAgent()).willReturn("chrome");

            // when
            RegisterAccountServiceResponse serviceResponse = registerAccountUseCase
                .registerAccount(command);
            Account account = findAccountPort.findByEmail(command.email());
            Token dbToken = findTokenPort.findByEmailAndUserAgent(command.email(), "chrome");
            Token cacheToken = findTokenCachePort
                .findByEmailAndUserAgent(command.email(), "chrome");

            // then
            assertThat(account.getEmail()).isEqualTo(command.email());
            assertThat(account.getAddress()).isEqualTo(command.address());
            assertThat(account.getPassword()).isEqualTo(aesUtil.encryptText(command.password()));
            assertThat(account.getUserTel()).isEqualTo(command.userTel());
            assertThat(account.getRole().name()).isEqualTo(command.role());
            assertThat(cacheToken.getRefreshToken()).isEqualTo(serviceResponse.refreshToken());
            assertThat(cacheToken.getEmail()).isEqualTo(command.email());
            assertThat(cacheToken.getUserAgent()).isEqualTo("chrome");
            assertThat(cacheToken.getRole()).isEqualTo(command.role());
            assertThat(cacheToken.getRegDateTime()).isEqualTo(dbToken.getRegDateTime());
            assertThat(dbToken.getRefreshToken()).isEqualTo(serviceResponse.refreshToken());
            assertThat(dbToken.getEmail()).isEqualTo(command.email());
            assertThat(dbToken.getUserAgent()).isEqualTo("chrome");
            assertThat(dbToken.getRole()).isEqualTo(command.role());
            deleteAccountPort.deleteByEmail(command.email());
            deleteTokenPort.deleteByEmail(command.email());
            redisTemplate.delete(command.email() + "-chrome::token");
        }

        @Test
        @DisplayName("[error] 등록된 사용자 정보라면 CustomBusinessException 을 응답한다.")
        void error() {
            // given
            RegisterAccountCommand command = RegisterAccountCommand.builder()
                .email("registerAccount.error")
                .address("registerAccount.error")
                .password("registerAccount.error")
                .userTel("01012341234")
                .role("ROLE_CUSTOMER")
                .build();
            given(userAgentUtil.getUserAgent()).willReturn("chrome");
            registerAccountUseCase.registerAccount(command);

            // when
            CustomBusinessException exception = assertThrows(CustomBusinessException.class,
                () -> registerAccountUseCase.registerAccount(command));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.Business_SAVED_ACCOUNT_INFO);
            deleteAccountPort.deleteByEmail(command.email());
            deleteTokenPort.deleteByEmail(command.email());
            redisTemplate.delete(command.email() + "-chrome::token");
        }
    }
}
package com.sweettracker.account.account.application.service.delete_account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.account.application.port.out.FindAccountHistoryPort;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterAccountPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.AccountHistory;
import com.sweettracker.account.account.domain.Role;
import com.sweettracker.account.global.exception.CustomNotFoundException;
import com.sweettracker.account.global.exception.ErrorCode;
import com.sweettracker.account.global.util.AesUtil;
import com.sweettracker.account.global.util.JwtUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class DeleteAccountServiceTest extends IntegrationTestSupport {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AesUtil aesUtil;
    @Autowired
    DeleteAccountService deleteAccountService;
    @Autowired
    RegisterAccountPort registerAccountPort;
    @Autowired
    FindAccountPort findAccountPort;
    @Autowired
    FindAccountHistoryPort findAccountHistoryPort;

    @Nested
    @DisplayName("[deleteAccount] 사용자 정보를 삭제하는 메소드")
    class Describe_deleteAccount {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있다면 정보를 삭제하고 히스토리를 등록하고 메시지를 전송하는지 확인한다.")
        void success(CapturedOutput output) throws InterruptedException {
            // given
            Account account = Account.builder()
                .email("od@test.com")
                .username("od")
                .regDateTime(LocalDateTime.now())
                .regDate("20240101")
                .userTel("01012341234")
                .role(Role.ROLE_CUSTOMER)
                .password(aesUtil.encryptText("1234"))
                .build();
            Account savedAccount = registerAccountPort.register(account);
            String authentication = jwtUtil.createAccessToken(savedAccount);

            // when
            DeleteAccountServiceResponse response = deleteAccountService
                .deleteAccount(authentication);
            boolean existsByEmail = findAccountPort.existsByEmail(account.getEmail());
            AccountHistory history = findAccountHistoryPort.findByEmail(account.getEmail());
            Thread.sleep(1000);
            
            // then
            assertThat(existsByEmail).isFalse();
            assertThat(response.id()).isEqualTo(history.getAccountId());
            assertThat(response.email()).isEqualTo(history.getEmail());
            assertThat(response.result()).isEqualTo("Y");
            assertThat(output.toString()).contains("[account-delete-to-order] ==>");
            assertThat(output.toString()).contains("[account-delete-to-delivery] ==>");
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없다면 예외를 응답하는지 확인한다.")
        void error1() {
            // given
            Account account = Account.builder()
                .email("od@test.com")
                .username("od")
                .regDateTime(LocalDateTime.now())
                .regDate("20240101")
                .userTel("01012341234")
                .role(Role.ROLE_CUSTOMER)
                .password(aesUtil.encryptText("1234"))
                .build();
            String authentication = jwtUtil.createAccessToken(account);

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> deleteAccountService.deleteAccount(authentication));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }
    }
}
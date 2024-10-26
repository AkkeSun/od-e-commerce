package com.sweettracker.account.account.application.service.update_account;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.account.application.port.in.command.UpdateAccountCommand;
import com.sweettracker.account.account.application.port.out.DeleteAccountHistoryPort;
import com.sweettracker.account.account.application.port.out.DeleteAccountPort;
import com.sweettracker.account.account.application.port.out.FindAccountHistoryPort;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterAccountPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.AccountHistory;
import com.sweettracker.account.account.domain.Role;
import com.sweettracker.account.global.util.AesUtil;
import com.sweettracker.account.global.util.JwtUtil;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UpdateAccountServiceTest extends IntegrationTestSupport {

    @Autowired
    AesUtil aesUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UpdateAccountService updateAccountService;
    @Autowired
    RegisterAccountPort registerAccountPort;
    @Autowired
    FindAccountPort findAccountPort;
    @Autowired
    DeleteAccountPort deleteAccountPort;
    @Autowired
    FindAccountHistoryPort findAccountHistoryPort;
    @Autowired
    DeleteAccountHistoryPort deleteAccountHistoryPort;

    @Nested
    @DisplayName("[updateAccount] 사용자 정보를 수정하는 메소드")
    class Describe_updateAccount {

        @Test
        @DisplayName("[success] 저장된 사용자 정보와 입력받은 사용자 정보가 다른 경우 사용자 정보를 수정하고 히스토리를 저장하며 수정된 정보를 반환한다.")
        void success() {
            // given
            Account account = Account.builder()
                .id(1L)
                .email("od@updateTest.com")
                .password("1234")
                .role(Role.ROLE_SELLER)
                .regDate("20240201")
                .regDateTime(LocalDateTime.now())
                .build();
            registerAccountPort.register(account);
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .accessToken(jwtUtil.createAccessToken(account))
                .username("updateAccount.username")
                .password("updateAccount.password")
                .userTel("updateAccount.userTel")
                .address("updateAccount.address")
                .build();
            Account savedAccount = findAccountPort.findByEmail(account.getEmail());

            // when
            UpdateAccountServiceResponse response = updateAccountService.updateAccount(command);
            Account updatedAccount = findAccountPort.findByEmail(account.getEmail());
            AccountHistory history = findAccountHistoryPort.findByEmail(account.getEmail());

            // then
            assertThat(response.updateYn()).isEqualTo("Y");
            assertThat(response.updateList().contains("username")).isTrue();
            assertThat(response.updateList().contains("password")).isTrue();
            assertThat(response.updateList().contains("userTel")).isTrue();
            assertThat(response.updateList().contains("address")).isTrue();
            assertThat(savedAccount.getUsername()).isNull();
            assertThat(savedAccount.getPassword()).isEqualTo("1234");
            assertThat(savedAccount.getUserTel()).isNull();
            assertThat(savedAccount.getAddress()).isNull();
            assertThat(updatedAccount.getUsername()).isEqualTo(command.username());
            assertThat(updatedAccount.getPassword()).isEqualTo(
                aesUtil.encryptText(command.password()));
            assertThat(updatedAccount.getUserTel()).isEqualTo(command.userTel());
            assertThat(updatedAccount.getAddress()).isEqualTo(command.address());
            assertThat(history.getAccountId()).isEqualTo(updatedAccount.getId());
            assertThat(history.getEmail()).isEqualTo(updatedAccount.getEmail());
            assertThat(history.getType()).isEqualTo("UPDATE");
            assertThat(history.getDetailInfo()).isEqualTo("username,password,userTel,address");
            deleteAccountPort.deleteByEmail(account.getEmail());
            deleteAccountHistoryPort.deleteByEmail(account.getEmail());
        }

        @Test
        @DisplayName("[success] 저장된 사용자 정보와 입력받은 사용자 정보가 같은 경우 사용자 정보를 수정하지 않고 응답값을 반환한다.")
        void success2() {
            // given
            Account account = Account.builder()
                .id(1L)
                .email("od@updateTest.com")
                .password("1234")
                .role(Role.ROLE_SELLER)
                .regDate("20240201")
                .regDateTime(LocalDateTime.now())
                .build();
            registerAccountPort.register(account);
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .accessToken(jwtUtil.createAccessToken(account))
                .username(account.getUsername())
                .password("1234")
                .userTel(account.getUserTel())
                .address(account.getAddress())
                .build();
            Account savedAccount = findAccountPort.findByEmail(account.getEmail());

            // when
            UpdateAccountServiceResponse response = updateAccountService.updateAccount(command);
            Account updatedAccount = findAccountPort.findByEmail(account.getEmail());
            AccountHistory history = findAccountHistoryPort.findByEmail(account.getEmail());

            // then
            assertThat(response.updateYn()).isEqualTo("N");
            assertThat(response.updateList().isEmpty()).isTrue();
            assertThat(savedAccount.getUsername()).isEqualTo(updatedAccount.getUsername());
            assertThat(savedAccount.getPassword()).isEqualTo(updatedAccount.getPassword());
            assertThat(savedAccount.getUserTel()).isEqualTo(updatedAccount.getUserTel());
            assertThat(savedAccount.getAddress()).isEqualTo(updatedAccount.getAddress());
            assertThat(history).isNull();
            deleteAccountPort.deleteByEmail(account.getEmail());
        }
    }
}
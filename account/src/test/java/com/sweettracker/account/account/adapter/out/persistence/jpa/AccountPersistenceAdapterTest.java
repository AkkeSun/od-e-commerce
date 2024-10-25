package com.sweettracker.account.account.adapter.out.persistence.jpa;

import static com.sweettracker.account.global.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.Role;
import com.sweettracker.account.global.exception.CustomNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccountPersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    AccountPersistenceAdapter accountPersistenceAdapter;

    @Nested
    @DisplayName("[findByEmail] 이메일로 사용자 정보를 조회하는 메소드")
    class Describe_findByEmail {

        @Test
        @DisplayName("[success] 조회된 정보가 있다면 사용자 정보를 응답한다.")
        void success() {
            // given
            String email = "findByEmail.success";
            Account account = Account.builder()
                .email(email)
                .address("findByEmail.success")
                .password("findByEmail.success")
                .userTel("findByEmail.success")
                .role(Role.ROLE_CUSTOMER)
                .build();
            accountPersistenceAdapter.register(account);

            // when
            Account result = accountPersistenceAdapter.findByEmail(email);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(account.getEmail());
            assertThat(result.getAddress()).isEqualTo(account.getAddress());
            assertThat(result.getPassword()).isEqualTo(account.getPassword());
            assertThat(result.getUserTel()).isEqualTo(account.getUserTel());
            assertThat(result.getRole()).isEqualTo(account.getRole());
            accountPersistenceAdapter.deleteByEmail(email);
        }

        @Test
        @DisplayName("[error] 조회된 정보가 없드면 CustomNotFoundException 을 응답한다")
        void error() {
            // given
            String email = "findByEmail.error";

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> accountPersistenceAdapter.findByEmail(email));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(DoesNotExist_ACCOUNT_INFO);
        }
    }

    @Nested
    @DisplayName("[findByEmailAndPassword] 이메일과 비밀번호로 사용자 정보를 조회하는 메소드")
    class Describe_findByEmailAndPassword {

        @Test
        @DisplayName("[success] 조회된 정보가 있다면 사용자 정보를 응답한다.")
        void success() {
            // given
            String email = "findByEmailAndPassword.success";
            String password = "findByEmailAndPassword.success";
            Account account = Account.builder()
                .email(email)
                .password(password)
                .address("findByEmailAndPassword.success")
                .userTel("findByEmailAndPassword.success")
                .role(Role.ROLE_CUSTOMER)
                .build();
            accountPersistenceAdapter.register(account);

            // when
            Account result = accountPersistenceAdapter.findByEmailAndPassword(email, password);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(account.getEmail());
            assertThat(result.getAddress()).isEqualTo(account.getAddress());
            assertThat(result.getPassword()).isEqualTo(account.getPassword());
            assertThat(result.getUserTel()).isEqualTo(account.getUserTel());
            assertThat(result.getRole()).isEqualTo(account.getRole());
            accountPersistenceAdapter.deleteByEmail(email);
        }

        @Test
        @DisplayName("[error] 조회된 정보가 없드면 CustomNotFoundException 을 응답한다")
        void error() {
            // given
            String email = "findByEmailAndPassword.error";
            String password = "findByEmailAndPassword.error";

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> accountPersistenceAdapter.findByEmailAndPassword(email, password));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(DoesNotExist_ACCOUNT_INFO);
        }
    }

    @Nested
    @DisplayName("[register] 사용자 정보를 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 사용자 정보가 정상적으로 등록되는지 확인한다.")
        void success() {
            // given
            String email = "register.success";
            Account account = Account.builder()
                .email(email)
                .password("register.success")
                .address("register.success")
                .userTel("register.success")
                .role(Role.ROLE_CUSTOMER)
                .build();

            // when
            accountPersistenceAdapter.register(account);
            Account result = accountPersistenceAdapter.findByEmail(email);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(account.getEmail());
            assertThat(result.getAddress()).isEqualTo(account.getAddress());
            assertThat(result.getPassword()).isEqualTo(account.getPassword());
            assertThat(result.getUserTel()).isEqualTo(account.getUserTel());
            assertThat(result.getRole()).isEqualTo(account.getRole());
            accountPersistenceAdapter.deleteByEmail(email);
        }
    }

    @Nested
    @DisplayName("[deleteByEmail] 이메일로 사용자 정보를 삭제하는 메소드")
    class Describe_deleteByEmail {

        @Test
        @DisplayName("[success] 조회된 정보가 있다면 사용자 정보를 삭제한다.")
        void success() {
            // given
            String email = "deleteByEmail.success";
            Account account = Account.builder()
                .email(email)
                .password("deleteByEmail.success")
                .address("deleteByEmail.success")
                .userTel("deleteByEmail.success")
                .role(Role.ROLE_CUSTOMER)
                .build();
            accountPersistenceAdapter.register(account);
            Account result = accountPersistenceAdapter.findByEmail(email);

            // when
            accountPersistenceAdapter.deleteByEmail(email);
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> accountPersistenceAdapter.findByEmail(email));

            // then
            assertThat(result).isNotNull();
            assertThat(exception.getErrorCode()).isEqualTo(DoesNotExist_ACCOUNT_INFO);
        }
    }

    @Nested
    @DisplayName("[existsByEmail] 저장된 사용자 정보가 있는지 조회하는 메소드")
    class Describe_existsByEmail {

        @Test
        @DisplayName("[success] 조회된 정보가 있다면 true 를 응답한다.")
        void success() {
            // given
            String email = "existsByEmail.success";
            Account account = Account.builder()
                .email(email)
                .password("existsByEmail.success")
                .address("existsByEmail.success")
                .userTel("existsByEmail.success")
                .role(Role.ROLE_CUSTOMER)
                .build();
            accountPersistenceAdapter.register(account);

            // when
            boolean result = accountPersistenceAdapter.existsByEmail(email);

            // then
            assertThat(result).isTrue();
            accountPersistenceAdapter.deleteByEmail(email);
        }

        @Test
        @DisplayName("[success] 조회된 정보가 앖디먄 false 를 응답한다.")
        void success2() {
            // given
            String email = "existsByEmail.success2";

            // when
            boolean result = accountPersistenceAdapter.existsByEmail(email);

            // then
            assertThat(result).isFalse();
        }
    }
}
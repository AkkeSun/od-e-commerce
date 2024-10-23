package com.sweettracker.account.account.adapter.out.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AccountMapperTest {

    AccountMapper mapper = new AccountMapper();

    @Nested
    @DisplayName("[toDomain] entity 를 domain 으로 변환하는 메소드")
    class Describe_toDomain {

        @Test
        @DisplayName("[SUCCESS] entity 가 domain 으로 잘 변환되는지 확인한다.")
        void toDomain_success() {
            // given
            AccountEntity entity = AccountEntity.builder()
                .id(10L)
                .email("testEmail")
                .password("testPassword")
                .username("testUsername")
                .userTel("testUserTel")
                .address("testAddress")
                .role(Role.ROLE_CUSTOMER)
                .build();

            // when
            Account domain = mapper.toDomain(entity);

            // then
            assertThat(domain.getId()).isEqualTo(entity.getId());
            assertThat(domain.getEmail()).isEqualTo(entity.getEmail());
            assertThat(domain.getPassword()).isEqualTo(entity.getPassword());
            assertThat(domain.getUsername()).isEqualTo(entity.getUsername());
            assertThat(domain.getUserTel()).isEqualTo(entity.getUserTel());
            assertThat(domain.getAddress()).isEqualTo(entity.getAddress());
            assertThat(domain.getRole()).isEqualTo(entity.getRole());
        }
    }

    @Nested
    @DisplayName("[toEntity] domain 을 entity 로 변환하는 메소드")
    class Describe_toEntity {

        @Test
        @DisplayName("[SUCCESS] domain 이 entity 로 잘 변환되는지 확인한다.")
        void toDomain_success() {
            // given
            Account domain = Account.builder()
                .id(10L)
                .email("testEmail")
                .password("testPassword")
                .username("testUsername")
                .userTel("testUserTel")
                .address("testAddress")
                .role(Role.ROLE_CUSTOMER)
                .build();

            // when
            AccountEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.getId());
            assertThat(entity.getEmail()).isEqualTo(domain.getEmail());
            assertThat(entity.getPassword()).isEqualTo(domain.getPassword());
            assertThat(entity.getUsername()).isEqualTo(domain.getUsername());
            assertThat(entity.getUserTel()).isEqualTo(domain.getUserTel());
            assertThat(entity.getAddress()).isEqualTo(domain.getAddress());
            assertThat(entity.getRole()).isEqualTo(domain.getRole());
        }
    }
}
package com.sweettracker.account.account.adapter.out.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.account.domain.AccountHistory;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AccountHistoryMapperTest {

    AccountHistoryMapper mapper = new AccountHistoryMapper();

    @Nested
    @DisplayName("[toDomain] entity 를 domain 으로 변환하는 메소드")
    class Describe_toDomain {

        @Test
        @DisplayName("[SUCCESS] entity 가 domain 으로 잘 변환되는지 확인한다.")
        void toDomain_success() {
            // given
            AccountHistoryEntity entity = AccountHistoryEntity.builder()
                .id(10L)
                .accountId(1L)
                .email("testEmail")
                .type("testType")
                .detailInfo("testDetailInfo")
                .regDate("2021-01-01")
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            AccountHistory domain = mapper.toDomain(entity);

            // then
            assertThat(domain.getId()).isEqualTo(entity.getId());
            assertThat(domain.getAccountId()).isEqualTo(entity.getAccountId());
            assertThat(domain.getEmail()).isEqualTo(entity.getEmail());
            assertThat(domain.getType()).isEqualTo(entity.getType());
            assertThat(domain.getDetailInfo()).isEqualTo(entity.getDetailInfo());
            assertThat(domain.getRegDate()).isEqualTo(entity.getRegDate());
            assertThat(domain.getRegDateTime()).isEqualTo(entity.getRegDateTime());
        }
    }

    @Nested
    @DisplayName("[toEntity] domain 을 entity 로 변환하는 메소드")
    class Describe_toEntity {

        @Test
        @DisplayName("[SUCCESS] domain 이 entity 로 잘 변환되는지 확인한다.")
        void toDomain_success() {
            // given
            AccountHistory domain = AccountHistory.builder()
                .id(10L)
                .accountId(1L)
                .email("testEmail")
                .type("testType")
                .detailInfo("testDetailInfo")
                .regDate("2021-01-01")
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            AccountHistoryEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.getId());
            assertThat(entity.getAccountId()).isEqualTo(domain.getAccountId());
            assertThat(entity.getEmail()).isEqualTo(domain.getEmail());
            assertThat(entity.getType()).isEqualTo(domain.getType());
            assertThat(entity.getDetailInfo()).isEqualTo(domain.getDetailInfo());
            assertThat(entity.getRegDate()).isEqualTo(domain.getRegDate());
            assertThat(entity.getRegDateTime()).isEqualTo(domain.getRegDateTime());
        }

    }
}
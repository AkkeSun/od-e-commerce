package com.sweettracker.account.account.adapter.out.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.IntegrationTestSupport;
import com.sweettracker.account.account.domain.AccountHistory;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccountHistoryPersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    AccountHistoryPersistenceAdapter accountHistoryPersistenceAdapter;
    @Autowired
    AccountHistoryRepository accountHistoryRepository;

    @Nested
    @DisplayName("[registerAccountHistory] 사용자 계정 히스토리를 등록하는 메소드")
    class Describe_registerAccountHistory {

        @Test
        @DisplayName("[success] 사용자 계정 히스토리가 정상적으로 등록되는지 확인한다.")
        void success() {
            // given
            AccountHistory accountHistory = AccountHistory.builder()
                .accountId(1L)
                .email("od@test.com")
                .type("UPDATE")
                .detailInfo("이메일 변경")
                .regDate("20241212")
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            accountHistoryPersistenceAdapter.registerAccountHistory(accountHistory);
            AccountHistoryEntity savedHistory = accountHistoryRepository
                .findByEmail(accountHistory.getEmail()).get();

            // then
            assertThat(savedHistory.getEmail()).isEqualTo(accountHistory.getEmail());
            assertThat(savedHistory.getType()).isEqualTo(accountHistory.getType());
            assertThat(savedHistory.getDetailInfo()).isEqualTo(accountHistory.getDetailInfo());
            assertThat(savedHistory.getRegDate()).isEqualTo(accountHistory.getRegDate());
            accountHistoryRepository.deleteAll();
        }
    }

    @Nested
    @DisplayName("[findByEmail] 이메일로 사용자 계정 히스토리를 조회하는 메소드")
    class Describe_findByEmail {

        @Test
        @DisplayName("[success] 조회된 정보가 있는경우 히스토리를 반환한다.")
        void success() {
            // given
            AccountHistory accountHistory = AccountHistory.builder()
                .accountId(1L)
                .email("od@test.com")
                .type("UPDATE")
                .detailInfo("이메일 변경")
                .regDate("20241212")
                .regDateTime(LocalDateTime.now())
                .build();
            accountHistoryPersistenceAdapter.registerAccountHistory(accountHistory);

            // when
            AccountHistory result = accountHistoryPersistenceAdapter
                .findByEmail(accountHistory.getEmail());

            // then
            assertThat(result.getEmail()).isEqualTo(accountHistory.getEmail());
            assertThat(result.getType()).isEqualTo(accountHistory.getType());
            assertThat(result.getDetailInfo()).isEqualTo(accountHistory.getDetailInfo());
            assertThat(result.getRegDate()).isEqualTo(accountHistory.getRegDate());
            accountHistoryRepository.deleteAll();
        }

        @Test
        @DisplayName("[success] 조회된 정보가 없는 경우 null 을 반환한다.")
        void success2() {
            // given
            AccountHistory accountHistory = AccountHistory.builder()
                .accountId(1L)
                .email("od@test2.com")
                .type("UPDATE")
                .detailInfo("이메일 변경")
                .regDate("20241212")
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            AccountHistory result = accountHistoryPersistenceAdapter
                .findByEmail(accountHistory.getEmail());

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("[deleteByEmail] 이메일로 사용자 히스토리를 삭제하는 메소드")
    class Describe_deleteByEmail {

        @Test
        @DisplayName("[success] 사용자 히스토리를 정상적으로 삭제하는지 확인한다.")
        void success() {
            // given
            AccountHistory accountHistory = AccountHistory.builder()
                .accountId(1L)
                .email("od@test2.com")
                .type("UPDATE")
                .detailInfo("이메일 변경")
                .regDate("20241212")
                .regDateTime(LocalDateTime.now())
                .build();
            accountHistoryPersistenceAdapter.registerAccountHistory(accountHistory);

            //when
            accountHistoryPersistenceAdapter.deleteByEmail(accountHistory.getEmail());

            //then
            assertThat(accountHistoryRepository.findByEmail(accountHistory.getEmail())).isEmpty();
        }
    }
}
package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.domain.AccountHistory;
import org.springframework.stereotype.Component;

@Component
class AccountHistoryMapper {

    public AccountHistoryEntity toEntity(AccountHistory domain) {
        return AccountHistoryEntity.builder()
            .id(domain.getId())
            .accountId(domain.getAccountId())
            .email(domain.getEmail())
            .type(domain.getType())
            .detailInfo(domain.getDetailInfo())
            .regDate(domain.getRegDate())
            .regDateTime(domain.getRegDateTime())
            .build();

    }
}

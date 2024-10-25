package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.domain.Account;
import org.springframework.stereotype.Component;

@Component
class AccountMapper {

    public Account toDomain(AccountEntity entity) {
        return Account.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .username(entity.getUsername())
            .userTel(entity.getUserTel())
            .address(entity.getAddress())
            .role(entity.getRole())
            .regDateTime(entity.getRegDateTime())
            .regDate(entity.getRegDate())
            .build();
    }

    public AccountEntity toEntity(Account domain) {
        return AccountEntity.builder()
            .id(domain.getId())
            .email(domain.getEmail())
            .password(domain.getPassword())
            .username(domain.getUsername())
            .userTel(domain.getUserTel())
            .address(domain.getAddress())
            .role(domain.getRole())
            .regDateTime(domain.getRegDateTime())
            .regDate(domain.getRegDate())
            .build();
    }
}

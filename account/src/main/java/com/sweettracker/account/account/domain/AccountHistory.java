package com.sweettracker.account.account.domain;

import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountHistory {

    private Long id;

    private Long accountId;

    private String email;

    private String type;

    private String detailInfo;

    private String regDate;

    private LocalDateTime regDateTime;

    @Builder
    public AccountHistory(Long id, Long accountId, String email, String type, String detailInfo,
        String regDate, LocalDateTime regDateTime) {
        this.id = id;
        this.accountId = accountId;
        this.email = email;
        this.type = type;
        this.detailInfo = detailInfo;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }

    public AccountHistory of(Claims claims) {
        return AccountHistory.builder()
            .email(claims.getSubject())
            .accountId(Long.valueOf((Integer) claims.get("accountId")))
            .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build();
    }

    public AccountHistory of(Account account) {
        return AccountHistory.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build();
    }

    public void updateType(String type) {
        this.type = type;
    }

    public void updateDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }
}

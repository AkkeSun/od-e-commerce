package com.sweettracker.account.account.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ACCOUNT_HISTORY")
@NoArgsConstructor
class AccountHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_INDEX")
    private Long id;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "HISTORY_TYPE")
    private String type;

    @Column(name = "DETAIL_INFO")
    private String detailInfo;

    @Column(name = "REG_DATE")
    private String regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    public AccountHistoryEntity(Long id, Long accountId, String email, String type,
        String detailInfo, String regDate, LocalDateTime regDateTime) {
        this.id = id;
        this.accountId = accountId;
        this.email = email;
        this.type = type;
        this.detailInfo = detailInfo;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}

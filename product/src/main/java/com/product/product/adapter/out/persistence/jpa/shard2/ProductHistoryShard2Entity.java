package com.product.product.adapter.out.persistence.jpa.shard2;

import com.product.product.domain.HistoryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "PRODUCT_HISTORY")
@NoArgsConstructor
class ProductHistoryShard2Entity {

    @Id
    @Column(name = "TABLE_INDEX")
    private long id;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "TYPE")
    @Enumerated(EnumType.STRING)
    private HistoryType type;

    @Column(name = "DETAIL_INFO")
    private String detailInfo;

    @Column(name = "REG_DATE")
    private String regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    ProductHistoryShard2Entity(long id, Long productId, Long accountId, HistoryType type,
        String detailInfo, String regDate, LocalDateTime regDateTime) {
        this.id = id;
        this.productId = productId;
        this.accountId = accountId;
        this.type = type;
        this.detailInfo = detailInfo;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}

package com.product.product.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductHistory {

    private long id;

    private Long productId;

    private Long accountId;

    private HistoryType type;

    private String detailInfo;

    private String regDate;

    private LocalDateTime regDateTime;

    @Builder
    public ProductHistory(long id, Long productId, Long accountId, HistoryType type,
        String detailInfo,
        String regDate, LocalDateTime regDateTime) {
        this.id = id;
        this.productId = productId;
        this.accountId = accountId;
        this.type = type;
        this.detailInfo = detailInfo;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}

package com.product.product.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductHistory {

    private Long id;
    private Long productId;
    private Long sellerId;
    private String sellerEmail;
    private HistoryType type;
    private String detailInfo;
    private String regDate;
    private LocalDateTime regDateTime;

    @Builder
    public ProductHistory(Long id, Long productId, Long sellerId, String sellerEmail,
        HistoryType type,
        String detailInfo, String regDate, LocalDateTime regDateTime) {
        this.id = id;
        this.productId = productId;
        this.sellerId = sellerId;
        this.sellerEmail = sellerEmail;
        this.type = type;
        this.detailInfo = detailInfo;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}

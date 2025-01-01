package com.product.review.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Review {

    private Long id;
    private Long productId;
    private Long accountId;
    private int score;
    private String comment;
    private String regDate;
    private LocalDateTime regDateTime;

    @Builder
    public Review(Long id, Long productId, Long accountId, int score, String comment,
        String regDate, LocalDateTime regDateTime) {
        this.id = id;
        this.productId = productId;
        this.accountId = accountId;
        this.score = score;
        this.comment = comment;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}

package com.product.review.adapter.out.persistence.jpa.shard1;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "REVIEW")
@NoArgsConstructor
class ReviewShard1Entity {

    @Id
    @Column(name = "TABLE_INDEX")
    private Long id;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "SCORE")
    private int score;

    @Column(name = "COMMENT")
    private String comment;

    @Column(name = "REG_DATE")
    private String regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    ReviewShard1Entity(Long id, Long productId, Long accountId, int score, String comment,
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

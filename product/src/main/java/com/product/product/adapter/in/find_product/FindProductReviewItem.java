package com.product.product.adapter.in.find_product;

import com.product.review.domain.Review;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindProductReviewItem {

    private Long writerId;
    private int score;
    private String comment;
    private String regDateTime;

    @Builder
    FindProductReviewItem(Long writerId, int score, String comment, String regDateTime) {
        this.writerId = writerId;
        this.score = score;
        this.comment = comment;
        this.regDateTime = regDateTime;
    }

    FindProductReviewItem of(Review review) {
        return FindProductReviewItem.builder()
            .writerId(review.getAccountId())
            .score(review.getScore())
            .comment(review.getComment())
            .regDateTime(review.getRegDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
    }
}

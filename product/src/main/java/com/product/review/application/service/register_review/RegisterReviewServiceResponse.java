package com.product.review.application.service.register_review;

import lombok.Builder;

@Builder
public record RegisterReviewServiceResponse(
    Long id,
    Long productId,
    Long accountId,
    int score,
    String comment,
    String regDate
) {

}

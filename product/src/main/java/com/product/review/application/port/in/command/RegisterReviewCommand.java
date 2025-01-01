package com.product.review.application.port.in.command;

import lombok.Builder;

@Builder
public record RegisterReviewCommand(
    Long productId,
    int score,
    String comment,
    String authorization
) {

}

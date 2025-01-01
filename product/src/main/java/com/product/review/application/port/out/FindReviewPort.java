package com.product.review.application.port.out;

public interface FindReviewPort {

    boolean existsByProductIdAndAccountId(Long ProductId, Long AccountId);
}

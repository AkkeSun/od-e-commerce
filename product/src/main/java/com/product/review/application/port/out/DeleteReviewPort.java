package com.product.review.application.port.out;

public interface DeleteReviewPort {

    void deleteByProductIdAndAccountId(Long productId, Long accountId);

    void deleteByProductId(Long accountId);
}

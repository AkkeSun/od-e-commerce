package com.product.review.application.port.out;

import com.product.review.domain.Review;

public interface RegisterReviewPort {

    Review register(Review review);
}

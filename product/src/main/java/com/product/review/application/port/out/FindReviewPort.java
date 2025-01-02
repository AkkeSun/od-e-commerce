package com.product.review.application.port.out;

import com.product.review.domain.Review;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface FindReviewPort {

    boolean existsByProductIdAndAccountId(Long ProductId, Long AccountId);

    List<Review> findByProductId(Long productId, Pageable pageable);
}

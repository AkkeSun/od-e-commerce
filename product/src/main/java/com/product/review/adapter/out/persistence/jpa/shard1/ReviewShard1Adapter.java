package com.product.review.adapter.out.persistence.jpa.shard1;

import com.product.review.application.port.out.DeleteReviewPort;
import com.product.review.application.port.out.FindReviewPort;
import com.product.review.application.port.out.RegisterReviewPort;
import com.product.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional("primaryTransactionManager")
public class ReviewShard1Adapter implements RegisterReviewPort, FindReviewPort, DeleteReviewPort {

    private final ReviewShard1Mapper reviewMapper;
    private final ReviewShard1Repository reviewRepository;

    @Override
    public Review register(Review review) {
        ReviewShard1Entity entity = reviewMapper.toEntity(review);
        return reviewMapper.toDomain(reviewRepository.save(entity));
    }

    @Override
    public boolean existsByProductIdAndAccountId(Long ProductId, Long AccountId) {
        return reviewRepository.existsByProductIdAndAccountId(ProductId, AccountId);
    }

    @Override
    public void deleteByProductIdAndAccountId(Long productId, Long accountId) {
        reviewRepository.deleteByProductIdAndAccountId(productId, accountId);
    }
}

package com.product.review.adapter.out.persistence.jpa.shard2;

import com.product.review.application.port.out.DeleteReviewPort;
import com.product.review.application.port.out.FindReviewPort;
import com.product.review.application.port.out.RegisterReviewPort;
import com.product.review.domain.Review;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional("secondaryTransactionManager")
public class ReviewShard2Adapter implements RegisterReviewPort, FindReviewPort, DeleteReviewPort {

    private final ReviewShard2Mapper reviewMapper;
    private final ReviewShard2Repository reviewRepository;

    @Override
    public Review register(Review review) {
        ReviewShard2Entity entity = reviewMapper.toEntity(review);
        return reviewMapper.toDomain(reviewRepository.save(entity));
    }

    @Override
    public boolean existsByProductIdAndAccountId(Long ProductId, Long AccountId) {
        return reviewRepository.existsByProductIdAndAccountId(ProductId, AccountId);
    }

    @Override
    public List<Review> findByProductId(Long productId, Pageable pageable) {
        return reviewRepository.findByProductId(productId, pageable).stream()
            .map(reviewMapper::toDomain)
            .toList();
    }

    @Override
    public void deleteByProductIdAndAccountId(Long productId, Long accountId) {
        reviewRepository.deleteByProductIdAndAccountId(productId, accountId);
    }
}

package com.product.review.adapter.out.persistence.jpa;

import com.product.global.util.ShardKeyUtil;
import com.product.review.adapter.out.persistence.jpa.shard1.ReviewShard1Adapter;
import com.product.review.adapter.out.persistence.jpa.shard2.ReviewShard2Adapter;
import com.product.review.application.port.out.DeleteReviewPort;
import com.product.review.application.port.out.FindReviewPort;
import com.product.review.application.port.out.RegisterReviewPort;
import com.product.review.domain.Review;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class ReviewPersistenceAdapter implements RegisterReviewPort, FindReviewPort, DeleteReviewPort {

    private final ShardKeyUtil shardKeyUtil;
    private final ReviewShard1Adapter reviewShard1Adapter;
    private final ReviewShard2Adapter reviewShard2Adapter;

    @Override
    public Review register(Review review) {
        return shardKeyUtil.isShard1(review.getProductId()) ?
            reviewShard1Adapter.register(review) : reviewShard2Adapter.register(review);
    }


    @Override
    public boolean existsByProductIdAndAccountId(Long ProductId, Long AccountId) {
        return shardKeyUtil.isShard1(ProductId) ?
            reviewShard1Adapter.existsByProductIdAndAccountId(ProductId, AccountId) :
            reviewShard2Adapter.existsByProductIdAndAccountId(ProductId, AccountId);
    }

    @Override
    public List<Review> findByProductId(Long productId, Pageable pageable) {
        return shardKeyUtil.isShard1(productId) ?
            reviewShard1Adapter.findByProductId(productId, pageable) :
            reviewShard2Adapter.findByProductId(productId, pageable);
    }

    @Override
    public void deleteByProductIdAndAccountId(Long productId, Long accountId) {
        if (shardKeyUtil.isShard1(productId)) {
            reviewShard1Adapter.deleteByProductIdAndAccountId(productId, accountId);
        } else {
            reviewShard2Adapter.deleteByProductIdAndAccountId(productId, accountId);
        }
    }
}

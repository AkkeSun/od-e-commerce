package com.product.review.adapter.out.persistence.jpa.shard2;

import com.product.review.domain.Review;
import org.springframework.stereotype.Component;

@Component
class ReviewShard2Mapper {

    ReviewShard2Entity toEntity(Review domain) {
        return ReviewShard2Entity.builder()
            .id(domain.getId())
            .productId(domain.getProductId())
            .accountId(domain.getAccountId())
            .comment(domain.getComment())
            .score(domain.getScore())
            .regDate(domain.getRegDate())
            .regDateTime(domain.getRegDateTime())
            .build();
    }

    Review toDomain(ReviewShard2Entity entity) {
        return Review.builder()
            .id(entity.getId())
            .productId(entity.getProductId())
            .accountId(entity.getAccountId())
            .comment(entity.getComment())
            .score(entity.getScore())
            .regDate(entity.getRegDate())
            .regDateTime(entity.getRegDateTime())
            .build();
    }
}

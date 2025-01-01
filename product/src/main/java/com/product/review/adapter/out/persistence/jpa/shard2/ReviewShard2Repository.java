package com.product.review.adapter.out.persistence.jpa.shard2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewShard2Repository extends JpaRepository<ReviewShard2Entity, Long> {

    boolean existsByProductIdAndAccountId(Long productId, Long accountId);
}

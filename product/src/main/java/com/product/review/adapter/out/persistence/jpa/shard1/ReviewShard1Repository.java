package com.product.review.adapter.out.persistence.jpa.shard1;

import org.springframework.data.jpa.repository.JpaRepository;

interface ReviewShard1Repository extends JpaRepository<ReviewShard1Entity, Long> {

    boolean existsByProductIdAndAccountId(Long productId, Long accountId);
}

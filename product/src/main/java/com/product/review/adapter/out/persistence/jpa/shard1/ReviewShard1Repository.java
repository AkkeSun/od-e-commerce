package com.product.review.adapter.out.persistence.jpa.shard1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ReviewShard1Repository extends JpaRepository<ReviewShard1Entity, Long> {

    boolean existsByProductIdAndAccountId(Long productId, Long accountId);

    @Modifying
    @Query("delete from ReviewShard1Entity r where r.productId = :productId and r.accountId = :accountId")
    void deleteByProductIdAndAccountId(Long productId, Long accountId);
}

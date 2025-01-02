package com.product.review.adapter.out.persistence.jpa.shard2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReviewShard2Repository extends JpaRepository<ReviewShard2Entity, Long> {

    boolean existsByProductIdAndAccountId(Long productId, Long accountId);

    @Modifying
    @Query("delete from ReviewShard2Entity r where r.productId = :productId and r.accountId = :accountId")
    void deleteByProductIdAndAccountId(Long productId, Long accountId);
}

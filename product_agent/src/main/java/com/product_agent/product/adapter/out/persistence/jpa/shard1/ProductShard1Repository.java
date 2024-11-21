package com.product_agent.product.adapter.out.persistence.jpa.shard1;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductShard1Repository extends JpaRepository<ProductShard1Entity, Long> {

    List<ProductShard1Entity> findByEmbeddingYn(String embeddingYn);

    @Modifying
    @Query("update ProductShard1Entity p set p.embeddingYn = :yn where p.productId = :productId")
    void updateEmbeddingYnByProductId(Long productId, String yn);
}

package com.product_agent.product.adapter.out.persistence.jpa.shard2;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductShard2Repository extends JpaRepository<ProductShard2Entity, Long> {

    List<ProductShard2Entity> findByEmbeddingYn(String embeddingYn);

    @Modifying
    @Query("update ProductShard2Entity p set p.embeddingYn = :yn where p.productId = :productId")
    void updateEmbeddingYnByProductId(Long productId, String yn);
}

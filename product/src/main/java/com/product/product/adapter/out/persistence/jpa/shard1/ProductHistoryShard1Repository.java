package com.product.product.adapter.out.persistence.jpa.shard1;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductHistoryShard1Repository extends
    JpaRepository<ProductHistoryShard1Entity, Long> {

    List<ProductHistoryShard1Entity> findByProductIdAndAccountId(Long productId, Long accountId);
}

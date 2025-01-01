package com.product.product.adapter.out.persistence.jpa.shard2;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductHistoryShard2Repository extends
    JpaRepository<ProductHistoryShard2Entity, Long> {

    List<ProductHistoryShard2Entity> findByProductIdAndAccountId(Long productId, Long accountId);
}

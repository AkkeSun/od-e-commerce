package com.product.product.adapter.out.persistence.jpa.shard2;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductHistoryShard2Repository extends
    JpaRepository<ProductHistoryShard2Entity, Long> {

    ProductHistoryShard2Entity findByProductIdAndAccountId(Long productId, Long accountId);
}

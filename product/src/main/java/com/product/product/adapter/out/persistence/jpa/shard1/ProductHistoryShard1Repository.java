package com.product.product.adapter.out.persistence.jpa.shard1;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProductHistoryShard1Repository extends
    JpaRepository<ProductHistoryShard1Entity, Long> {

}

package com.product.product.adapter.out.persistence.jpa.shard1;

import com.product.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductShard1Repository extends JpaRepository<ProductShard1Entity, Long> {

    @Modifying
    @Query("update ProductShard1Entity p "
        + "set p.salesCount = :#{#product.salesCount}, "
        + "    p.quantity = :#{#product.quantity} "
        + "where p.productId = :#{#product.productId} ")
    void updateSalesCountAndQuantity(Product product);
}

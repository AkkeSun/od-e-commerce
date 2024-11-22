package com.product.product.adapter.out.persistence.jpa.shard2;

import com.product.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ProductShard2Repository extends JpaRepository<ProductShard2Entity, Long> {

    @Modifying
    @Query("update ProductShard2Entity p "
        + "set p.salesCount = :#{#product.salesCount}, "
        + "    p.quantity = :#{#product.quantity} "
        + "where p.productId = :#{#product.productId} ")
    void updateSalesCountAndQuantity(Product product);

    @Modifying
    @Query("update ProductShard2Entity p "
        + "set p.quantity = :#{#product.quantity} "
        + "where p.productId = :#{#product.productId} ")
    void updateQuantity(Product product);
}

package com.product_agent.product.adapter.out.persistence.jpa.shard2;

import com.product_agent.product.domain.Product;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
class ProductShard2Mapper {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Product toDomain(ProductShard2Entity entity) {
        return Product.builder()
            .productId(entity.getProductId())
            .sellerEmail(entity.getSellerEmail())
            .productName(entity.getProductName())
            .productImg(entity.getProductImg())
            .description(entity.getDescription())
            .price(entity.getPrice())
            .category(entity.getCategory())
            .regDateTime(entity.getRegDateTime().format(formatter))
            .build();
    }
}

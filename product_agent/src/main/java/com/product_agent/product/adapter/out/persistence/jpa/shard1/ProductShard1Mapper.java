package com.product_agent.product.adapter.out.persistence.jpa.shard1;

import com.product_agent.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
class ProductShard1Mapper {

    Product toDomain(ProductShard1Entity entity) {
        return Product.builder()
            .productId(entity.getProductId())
            .productName(entity.getProductName())
            .price(entity.getPrice())
            .quantity(entity.getQuantity())
            .salesCount(entity.getSalesCount())
            .productImg(entity.getProductImg())
            .reviewCount(entity.getReviewCount())
            .description(entity.getDescription())
            .sellerEmail(entity.getSellerEmail())
            .hitCount(entity.getHitCount())
            .reviewScore(entity.getReviewScore())
            .totalScore(entity.getTotalScore())
            .category(entity.getCategory())
            .embeddingYn(entity.getEmbeddingYn())
            .regDate(entity.getRegDate())
            .regDateTime(entity.getRegDateTime())
            .build();
    }
}

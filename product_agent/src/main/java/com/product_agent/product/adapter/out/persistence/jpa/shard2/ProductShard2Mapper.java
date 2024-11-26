package com.product_agent.product.adapter.out.persistence.jpa.shard2;

import com.product_agent.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
class ProductShard2Mapper {

    Product toDomain(ProductShard2Entity entity) {
        return Product.builder()
            .productId(entity.getProductId())
            .productName(entity.getProductName())
            .price(entity.getPrice())
            .description(entity.getDescription())
            .sellerEmail(entity.getSellerEmail())
            .productImg(entity.getProductImg())
            .quantity(entity.getQuantity())
            .salesCount(entity.getSalesCount())
            .reviewCount(entity.getReviewCount())
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

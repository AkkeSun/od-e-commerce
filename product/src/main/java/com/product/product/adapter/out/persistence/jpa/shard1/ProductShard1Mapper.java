package com.product.product.adapter.out.persistence.jpa.shard1;

import com.product.global.util.JsonUtil;
import com.product.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductShard1Mapper {

    private final JsonUtil jsonUtil;

    ProductShard1Entity toEntity(Product product) {
        return ProductShard1Entity.builder()
            .productId(product.getProductId())
            .sellerId(product.getSellerId())
            .sellerEmail(product.getSellerEmail())
            .productName(product.getProductName())
            .productImg(product.getProductImg())
            .productOption(product.existProductOption() ?
                jsonUtil.toJsonString(product.getProductOption()) : null)
            .description(product.getDescription())
            .price(product.getPrice())
            .quantity(product.getQuantity())
            .salesCount(product.getSalesCount())
            .hitCount(product.getHitCount())
            .reviewCount(product.getReviewCount())
            .reviewScore(product.getReviewScore())
            .category(product.getCategory())
            .embeddingYn(product.getEmbeddingYn())
            .regDate(product.getRegDate())
            .regDateTime(product.getRegDateTime())
            .build();
    }

    Product toDomain(ProductShard1Entity entity) {
        return Product.builder()
            .productId(entity.getProductId())
            .sellerId(entity.getSellerId())
            .sellerEmail(entity.getSellerEmail())
            .productName(entity.getProductName())
            .productImg(entity.getProductImg())
            .productOption(entity.getProductOption() != null ?
                jsonUtil.parseJsonToList(entity.getProductOption(), String.class) : null)
            .description(entity.getDescription())
            .price(entity.getPrice())
            .quantity(entity.getQuantity())
            .salesCount(entity.getSalesCount())
            .hitCount(entity.getHitCount())
            .reviewCount(entity.getReviewCount())
            .reviewScore(entity.getReviewScore())
            .category(entity.getCategory())
            .embeddingYn(entity.getEmbeddingYn())
            .regDate(entity.getRegDate())
            .regDateTime(entity.getRegDateTime())
            .build();
    }
}

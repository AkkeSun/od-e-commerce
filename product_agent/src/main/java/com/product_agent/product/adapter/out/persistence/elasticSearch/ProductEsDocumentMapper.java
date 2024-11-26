package com.product_agent.product.adapter.out.persistence.elasticSearch;

import com.product_agent.product.domain.Product;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
class ProductEsDocumentMapper {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ProductEsDocument toDocument(Product product) {
        return ProductEsDocument.builder()
            .productId(product.getProductId())
            .productName(product.getProductName())
            .description(product.getDescription())
            .sellerEmail(product.getSellerEmail())
            .productImg(product.getProductImg())
            .price(product.getPrice())
            .salesCount(product.getSalesCount())
            .reviewCount(product.getReviewCount())
            .totalScore(product.getTotalScore())
            .category(product.getCategory())
            .regDateTime(product.getRegDateTime().format(formatter))
            .build();
    }

    Product toDomain(ProductEsDocument document) {
        return Product.builder()
            .productId(document.getProductId())
            .productName(document.getProductName())
            .description(document.getDescription())
            .sellerEmail(document.getSellerEmail())
            .productImg(document.getProductImg())
            .price(document.getPrice())
            .salesCount(document.getSalesCount())
            .reviewCount(document.getReviewCount())
            .totalScore(document.getTotalScore())
            .category(document.getCategory())
            .regDateTime(LocalDateTime.parse(document.getRegDateTime(), formatter))
            .build();
    }
}

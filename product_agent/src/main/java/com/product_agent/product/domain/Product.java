package com.product_agent.product.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Product {

    private Long productId;
    private long sellerId;
    private String sellerEmail;
    private String productName;
    private String productImg;
    private List<String> productOption;
    private String description;
    private long price;
    private long quantity;
    private long salesCount;
    private long reviewCount;
    private long hitCount;
    private double reviewScore;
    private double totalScore;
    private Category category;
    private String embeddingYn;
    private String regDate;
    private LocalDateTime regDateTime;

    @Builder
    public Product(Long productId, long sellerId, String sellerEmail, String productName,
        String productImg, List<String> productOption, String description, long price,
        long quantity,
        long salesCount, long reviewCount, long hitCount, double reviewScore, double totalScore,
        Category category, String embeddingYn, String regDate, LocalDateTime regDateTime) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.sellerEmail = sellerEmail;
        this.productName = productName;
        this.productImg = productImg;
        this.productOption = productOption;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.salesCount = salesCount;
        this.reviewCount = reviewCount;
        this.hitCount = hitCount;
        this.reviewScore = reviewScore;
        this.totalScore = totalScore;
        this.category = category;
        this.embeddingYn = embeddingYn;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}

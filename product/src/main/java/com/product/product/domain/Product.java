package com.product.product.domain;

import com.product.product.application.port.in.command.RegisterProductCommand;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private double reviewScore;
    private double totalScore;
    private Category category;
    private String embeddingYn;
    private String regDate;
    private LocalDateTime regDateTime;

    @Builder
    public Product(Long productId, long sellerId, String sellerEmail, String productName,
        String productImg, List<String> productOption, String description, long price,
        long quantity, long salesCount, long reviewCount, double reviewScore, double totalScore,
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
        this.reviewScore = reviewScore;
        this.totalScore = totalScore;
        this.category = category;
        this.embeddingYn = embeddingYn;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }

    public Product of(RegisterProductCommand command, Claims claims) {
        return Product.builder()
            .sellerId(claims.get("accountId", Integer.class))
            .sellerEmail(claims.getSubject())
            .productName(command.productName())
            .productImg(command.productImg())
            .productOption(command.productOption())
            .description(command.description())
            .price(command.price())
            .quantity(command.quantity())
            .reviewCount(0)
            .reviewScore(0)
            .totalScore(0)
            .embeddingYn("N")
            .category(Category.valueOf(command.category()))
            .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build();
    }

    public void updateProductId(Long productId) {
        this.productId = productId;
    }

    public boolean existProductOption() {
        return productOption != null && !productOption.isEmpty();
    }
}

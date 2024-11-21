package com.product_agent.product.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Product {

    private Long productId;

    private String productName;

    private String description;

    private String sellerEmail;

    private String productImg;

    private long price;

    private Category category;

    private String regDateTime;
    
    @Builder
    public Product(Long productId, String productName, String description, String sellerEmail,
        String productImg, long price, Category category, String regDateTime) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.sellerEmail = sellerEmail;
        this.productImg = productImg;
        this.price = price;
        this.category = category;
        this.regDateTime = regDateTime;
    }

    public void updateProductId(Long productId) {
        this.productId = productId;
    }
}

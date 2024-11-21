package com.product.product.adapter.in.resgister_product;

import com.product.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.product.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterProductResponse {
    private Long productId;
    private String sellerEmail;
    private String productName;
    private String productImg;
    private String description;
    private long price;
    private long quantity;
    private Category category;

    @Builder
    RegisterProductResponse(Long productId, String sellerEmail, String productName,
        String productImg, String description, long price, long quantity, Category category) {
        this.productId = productId;
        this.sellerEmail = sellerEmail;
        this.productName = productName;
        this.productImg = productImg;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    RegisterProductResponse of(RegisterProductServiceResponse serviceResponse) {
        return RegisterProductResponse.builder()
            .productId(serviceResponse.productId())
            .sellerEmail(serviceResponse.sellerEmail())
            .productName(serviceResponse.productName())
            .productImg(serviceResponse.productImg())
            .description(serviceResponse.description())
            .price(serviceResponse.price())
            .quantity(serviceResponse.quantity())
            .category(serviceResponse.category())
            .build();
    }
}

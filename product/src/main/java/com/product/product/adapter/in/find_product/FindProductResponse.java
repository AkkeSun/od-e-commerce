package com.product.product.adapter.in.find_product;

import com.product.product.application.service.find_product.FindProductServiceResponse;
import com.product.product.domain.Category;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindProductResponse {

    private Long productId;
    private long sellerId;
    private String sellerEmail;
    private String productName;
    private String productImg;
    private List<String> productOption;
    private String description;
    private long price;
    private Category category;
    private List<FindProductReviewItem> reviews;
    private String regDateTime;

    @Builder
    FindProductResponse(Long productId, long sellerId, String sellerEmail,
        String productName, String productImg, List<String> productOption, String description,
        long price, Category category, List<FindProductReviewItem> reviews, String regDateTime) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.sellerEmail = sellerEmail;
        this.productName = productName;
        this.productImg = productImg;
        this.productOption = productOption;
        this.description = description;
        this.price = price;
        this.category = category;
        this.reviews = reviews;
        this.regDateTime = regDateTime;
    }

    FindProductResponse of (FindProductServiceResponse serviceResponse) {
        return FindProductResponse.builder()
            .productId(serviceResponse.productId())
            .sellerId(serviceResponse.sellerId())
            .sellerEmail(serviceResponse.sellerEmail())
            .productName(serviceResponse.productName())
            .productImg(serviceResponse.productImg())
            .productOption(serviceResponse.productOption())
            .description(serviceResponse.description())
            .price(serviceResponse.price())
            .category(serviceResponse.category())
            .reviews(serviceResponse.reviews().stream()
                .map(review -> new FindProductReviewItem().of(review))
                .toList())
            .regDateTime(serviceResponse.regDateTime())
            .build();
    }
}

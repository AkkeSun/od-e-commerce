package com.product.product.adapter.in.find_product_list;

import com.product.product.application.service.find_product_list.FindProductListServiceResponse;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindProductListResponse {

    private Long productId;
    private String productName;
    private long price;
    private String category;
    private String productImg;
    private String sellerEmail;
    private LocalDateTime regDateTime;

    @Builder
    FindProductListResponse(Long productId, String productName, long price, String category,
        String productImg, String sellerEmail, LocalDateTime regDateTime) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.productImg = productImg;
        this.sellerEmail = sellerEmail;
        this.regDateTime = regDateTime;
    }

    FindProductListResponse of(FindProductListServiceResponse serviceResponse) {
        return FindProductListResponse.builder()
            .productId(serviceResponse.productId())
            .productName(serviceResponse.productName())
            .price(serviceResponse.price())
            .category(serviceResponse.category().name())
            .productImg(serviceResponse.productImg())
            .sellerEmail(serviceResponse.sellerEmail())
            .regDateTime(serviceResponse.regDateTime())
            .build();
    }
}

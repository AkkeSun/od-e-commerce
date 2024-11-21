package com.product.product.application.service.register_product;

import com.product.product.domain.Category;
import lombok.Builder;

@Builder
public record RegisterProductServiceResponse(
    Long productId,
    String sellerEmail,
    String productName,
    String productImg,
    String description,
    long price,
    long quantity,
    Category category
) {

}

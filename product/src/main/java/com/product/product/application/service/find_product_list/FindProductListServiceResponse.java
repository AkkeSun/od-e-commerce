package com.product.product.application.service.find_product_list;

import com.product.product.domain.Category;
import lombok.Builder;

@Builder
public record FindProductListServiceResponse(
    Long productId,
    String productName,
    long price,
    Category category,
    String productImg,
    String sellerEmail,
    String regDateTime
) {

}

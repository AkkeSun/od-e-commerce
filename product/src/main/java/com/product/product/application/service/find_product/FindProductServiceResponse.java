package com.product.product.application.service.find_product;

import com.product.product.domain.Category;
import com.product.review.domain.Review;
import java.util.List;
import lombok.Builder;

@Builder
public record FindProductServiceResponse(
     Long productId,
     long sellerId,
     String sellerEmail,
     String productName,
     String productImg,
     List<String>productOption,
     String description,
     long price,
     Category category,
     List<Review> reviews,
     String regDateTime
) {

}

package com.product.product.application.service.find_product;

import com.product.product.application.port.in.FindProductUseCase;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.domain.Product;
import com.product.review.application.port.out.FindReviewPort;
import com.product.review.domain.Review;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindProductService implements FindProductUseCase {

    private final FindProductPort findProductPort;
    private final FindReviewPort findReviewPort;

    @Override
    public FindProductServiceResponse findProduct(Long productId) {
        Product product = findProductPort.findById(productId);
        List<Review> reviews = findReviewPort
            .findByProductId(productId, PageRequest.of(0, 10));

        return FindProductServiceResponse.builder()
            .productId(product.getProductId())
            .sellerId(product.getSellerId())
            .sellerEmail(product.getSellerEmail())
            .productName(product.getProductName())
            .productImg(product.getProductImg())
            .productOption(product.getProductOption())
            .description(product.getDescription())
            .price(product.getPrice())
            .category(product.getCategory())
            .reviews(reviews)
            .regDateTime(product.getRegDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .build();
    }

}

package com.product.product.application.service.find_product;

import com.product.IntegrationTestSupport;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import com.product.review.application.port.out.DeleteReviewPort;
import com.product.review.application.port.out.RegisterReviewPort;
import com.product.review.domain.Review;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FindProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private FindProductService findProductService;
    @Autowired
    private RegisterProductPort registerProductPort;
    @Autowired
    private RegisterReviewPort registerReviewPort;
    @Autowired
    private DeleteProductPort deleteProductPort;
    @Autowired
    private DeleteReviewPort deleteReviewPort;

    @Nested
    @DisplayName("[findProduct] 상품을 조회하는 메소드")
    class Describe_findProduct {

        @Test
        @DisplayName("[success] 조회된 상품을 정상적으로 반환한다.")
        void success() {
            // given
            Product product = Product.builder()
                .productId(snowflakeGenerator.nextId())
                .sellerId(10L)
                .sellerEmail("od")
                .productName("od")
                .productImg("od")
                .description("od")
                .price(10)
                .quantity(1)
                .category(Category.BEAUTY)
                .embeddingYn("N")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build();
            registerProductPort.register(product);
            Review review = Review.builder()
                .id(snowflakeGenerator.nextId())
                .productId(product.getProductId())
                .accountId(1L)
                .score(5)
                .comment("좋아요")
                .regDate("20241112")
                .regDateTime(LocalDateTime.now())
                .build();
            registerReviewPort.register(review);

            // when
            FindProductServiceResponse result = findProductService.findProduct(
                product.getProductId());

            // then
            assert result.productId().equals(product.getProductId());
            assert result.sellerId() == product.getSellerId();
            assert result.sellerEmail().equals(product.getSellerEmail());
            assert result.productName().equals(product.getProductName());
            assert result.productImg().equals(product.getProductImg());
            assert result.description().equals(product.getDescription());
            assert result.price() == product.getPrice();
            assert result.category().equals(product.getCategory());
            assert result.reviews().size() == 1;
            assert result.reviews().get(0).getId().equals(review.getId());
            assert result.reviews().get(0).getProductId().equals(review.getProductId());
            assert result.reviews().get(0).getAccountId() == review.getAccountId();
            assert result.reviews().get(0).getScore() == review.getScore();
            assert result.reviews().get(0).getComment().equals(review.getComment());
            assert result.reviews().get(0).getRegDate().equals(review.getRegDate());

            // clean
            deleteReviewPort.deleteByProductIdAndAccountId(review.getProductId(),
                review.getAccountId());
            deleteProductPort.deleteById(product.getProductId());
        }
    }
}
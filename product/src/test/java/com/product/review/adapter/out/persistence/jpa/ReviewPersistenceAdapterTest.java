package com.product.review.adapter.out.persistence.jpa;

import com.product.IntegrationTestSupport;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import com.product.review.domain.Review;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReviewPersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    ReviewPersistenceAdapter reviewPersistenceAdapter;
    @Autowired
    RegisterProductPort registerProductPort;
    @Autowired
    DeleteProductPort deleteProductPort;

    @Nested
    @DisplayName("[register] 리뷰를 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 리뷰를 정상적으로 등록하는지 확인한다.")
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
                .comment("comment")
                .regDate("20241112")
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            reviewPersistenceAdapter.register(review);
            boolean result = reviewPersistenceAdapter.existsByProductIdAndAccountId(
                product.getProductId(), review.getAccountId());

            // then
            assert result;

            // clean
            reviewPersistenceAdapter.deleteByProductIdAndAccountId(product.getProductId(),
                review.getAccountId());
            deleteProductPort.deleteById(product.getProductId());
        }
    }

    @Nested
    @DisplayName("[existsByProductIdAndAccountId] 상품 아이디와 사용자 아이디로 리뷰 존재 유무를 확인하는 메소드")
    class Describe_existsByProductIdAndAccountId {

        @Test
        @DisplayName("[success] 존재하는 리뷰가 없다면 false 를 응답한다")
        void success1() {
            // given
            Long productId = 11L;
            Long accountId = 15L;

            // when
            boolean result = reviewPersistenceAdapter.existsByProductIdAndAccountId(productId,
                accountId);

            // then
            assert !result;
        }

        @Test
        @DisplayName("[success] 존재하는 리뷰가 있다면 true 를 응답한다")
        void success2() {
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
                .comment("comment")
                .regDate("20241112")
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            reviewPersistenceAdapter.register(review);
            boolean result = reviewPersistenceAdapter.existsByProductIdAndAccountId(
                product.getProductId(), review.getAccountId());

            // then
            assert result;

            // clean
            reviewPersistenceAdapter.deleteByProductIdAndAccountId(product.getProductId(),
                review.getAccountId());
            deleteProductPort.deleteById(product.getProductId());
        }
    }
}
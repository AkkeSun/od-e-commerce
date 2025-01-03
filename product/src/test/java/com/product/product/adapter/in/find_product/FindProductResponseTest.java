package com.product.product.adapter.in.find_product;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.product.application.service.find_product.FindProductServiceResponse;
import com.product.product.domain.Category;
import com.product.review.domain.Review;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객체를 API 응답 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답 객체가 API 응답 객체로 잘 변환되는지 확인한다.")
        void success() {
            // Given
            FindProductServiceResponse serviceResponse = FindProductServiceResponse.builder()
                .productId(12345L)
                .sellerId(12L)
                .sellerEmail("test seller")
                .productName("상품명")
                .productImg("상품 이미지")
                .productOption(List.of("옵션1", "옵션2"))
                .description("상품 설명")
                .price(10000)
                .category(Category.AUTOMOTIVE)
                .reviews(
                    Arrays.asList(Review.builder()
                            .id(1L)
                            .productId(12345L)
                            .accountId(1L)
                            .comment("리뷰 내용")
                            .score(5)
                            .regDateTime(LocalDateTime.now())
                            .build(),
                        Review.builder()
                            .id(2L)
                            .productId(12345L)
                            .accountId(2L)
                            .comment("리뷰 내용2")
                            .score(4)
                            .regDateTime(LocalDateTime.now())
                            .build()))
                .regDateTime("2021-08-01 12:23:12")
                .build();

            // when
            FindProductResponse response = new FindProductResponse().of(serviceResponse);

            // then
            assertThat(response.getProductId()).isEqualTo(serviceResponse.productId());
            assertThat(response.getSellerId()).isEqualTo(serviceResponse.sellerId());
            assertThat(response.getSellerEmail()).isEqualTo(serviceResponse.sellerEmail());
            assertThat(response.getProductName()).isEqualTo(serviceResponse.productName());
            assertThat(response.getProductImg()).isEqualTo(serviceResponse.productImg());
            assertThat(response.getProductOption()).isEqualTo(serviceResponse.productOption());
            assertThat(response.getDescription()).isEqualTo(serviceResponse.description());
            assertThat(response.getPrice()).isEqualTo(serviceResponse.price());
            assertThat(response.getCategory()).isEqualTo(serviceResponse.category());
            assertThat(response.getReviews().size()).isEqualTo(serviceResponse.reviews().size());
            assertThat(response.getRegDateTime()).isEqualTo(serviceResponse.regDateTime());
        }
    }
}
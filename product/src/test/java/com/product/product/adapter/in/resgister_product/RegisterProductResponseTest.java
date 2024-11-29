package com.product.product.adapter.in.resgister_product;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.product.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterProductResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객채를 API 응답 객채로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 서비스 응답객채가 API 응답 객채로 잘 변환되는지 확인한다.")
        void success() {
            // Given
            RegisterProductServiceResponse serviceResponse = RegisterProductServiceResponse.builder()
                .productId(12345L)
                .sellerEmail("od@test.com")
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .price(10000)
                .quantity(50)
                .category(Category.valueOf("AUTOMOTIVE"))
                .build();

            // When
            RegisterProductResponse response = new RegisterProductResponse().of(serviceResponse);

            // then
            assertThat(response.getProductId()).isEqualTo(serviceResponse.productId());
            assertThat(response.getSellerEmail()).isEqualTo(serviceResponse.sellerEmail());
            assertThat(response.getProductName()).isEqualTo(serviceResponse.productName());
            assertThat(response.getProductImg()).isEqualTo(serviceResponse.productImg());
            assertThat(response.getDescription()).isEqualTo(serviceResponse.description());
            assertThat(response.getPrice()).isEqualTo(serviceResponse.price());
            assertThat(response.getQuantity()).isEqualTo(serviceResponse.quantity());
            assertThat(response.getCategory()).isEqualTo(serviceResponse.category());
        }
    }
}
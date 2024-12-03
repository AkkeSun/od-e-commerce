package com.product.product.adapter.in.find_product_list;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.product.domain.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductListResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객채를 API 응답 객채로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 서비스 응답객채가 API 응답 객채로 잘 변환되는지 확인한다.")
        void success() {
            // Given
            FindProductListServiceResponse serviceResponse = FindProductListServiceResponse.builder()
                .productId(12345L)
                .sellerEmail("test seller")
                .productName("상품명")
                .productImg("상품 이미지")
                .price(10000)
                .category(Category.AUTOMOTIVE)
                .build();

            // when
            FindProductListResponse response = new FindProductListResponse().of(serviceResponse);

            // then
            assertThat(response.getProductId()).isEqualTo(serviceResponse.productId());
            assertThat(response.getSellerEmail()).isEqualTo(serviceResponse.sellerEmail());
            assertThat(response.getProductName()).isEqualTo(serviceResponse.productName());
            assertThat(response.getProductImg()).isEqualTo(serviceResponse.productImg());
            assertThat(response.getPrice()).isEqualTo(serviceResponse.price());
            assertThat(response.getCategory()).isEqualTo(serviceResponse.category().name());
        }
    }
}
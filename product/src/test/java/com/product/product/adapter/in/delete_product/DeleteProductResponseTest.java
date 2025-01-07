package com.product.product.adapter.in.delete_product;

import com.product.product.application.service.delete_product.DeleteProductServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeleteProductResponseTest {

    @Nested
    @DisplayName("[of] DeleteProductServiceResponse 로 DeleteProductResponse 를 생성하는 테스트")
    class Describe_of {

        @Test
        @DisplayName("[success] DeleteProductServiceResponse 로 DeleteProductResponse 를 생성한다.")
        void success() {
            // given
            DeleteProductServiceResponse serviceResponse = DeleteProductServiceResponse.builder()
                .result("Y")
                .build();

            // when
            DeleteProductResponse response = new DeleteProductResponse().of(serviceResponse);

            // then
            assert response.getResult().equals("Y");
        }
    }
}
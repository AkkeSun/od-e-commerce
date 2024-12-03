package com.product.product.adapter.in.update_product_quantity;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateProductQuantityResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답 객채를 API 응답 객채로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 서비스 응답객채가 API 응답 객채로 잘 변환되는지 확인한다.")
        void success() {
            // given
            UpdateProductQuantityServiceResponse serviceResponse = UpdateProductQuantityServiceResponse.builder()
                .result("Y")
                .build();

            // when
            UpdateProductQuantityResponse response = new UpdateProductQuantityResponse().of(
                serviceResponse);

            // then
            assertThat(response.getResult()).isEqualTo(serviceResponse.result());
        }
    }
}
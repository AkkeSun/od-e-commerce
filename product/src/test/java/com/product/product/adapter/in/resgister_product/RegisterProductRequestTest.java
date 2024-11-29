package com.product.product.adapter.in.resgister_product;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.product.application.port.in.command.RegisterProductCommand;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterProductRequestTest {

    @Nested
    @DisplayName("[toCommand] 입력 데이터를 command로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 입력 데이터가 command 로 잘 변환되는지 확인한다.")
        void success() {
            // Given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(50)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // When
            RegisterProductCommand command = request.toCommand(authorization);

            // then
            assertThat(command.authorization()).isEqualTo(authorization);
            assertThat(command.productName()).isEqualTo(request.getProductName());
            assertThat(command.productImg()).isEqualTo(request.getProductImg());
            assertThat(command.description()).isEqualTo(request.getDescription());
            assertThat(command.productOption()).isEqualTo(request.getProductOption());
            assertThat(command.price()).isEqualTo(request.getPrice());
            assertThat(command.quantity()).isEqualTo(request.getQuantity());
            assertThat(command.category()).isEqualTo(request.getCategory());
        }
    }

}
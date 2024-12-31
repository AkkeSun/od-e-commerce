package com.product.product.adapter.in.update_product_quantity;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.product.domain.QuantityType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateProductQuantityRequestTest {

    @Nested
    @DisplayName("[toCommand] 입력 데이터를 command로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 입력 데이터가 command 로 잘 변환되는지 확인한다.")
        void success() {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .productCount(50)
                .updateType("ADD_QUANTITY")
                .build();
            Long productId = 12345L;
            String authorization = "testToken";

            // when
            UpdateProductQuantityCommand command = request.toCommand(productId, authorization);

            // then
            assertThat(command.productId()).isEqualTo(productId);
            assertThat(command.authorization()).isEqualTo(authorization);
            assertThat(command.productCount()).isEqualTo(request.getProductCount());
            assertThat(command.updateType()).isEqualTo(QuantityType.ADD_QUANTITY);
        }
    }
}
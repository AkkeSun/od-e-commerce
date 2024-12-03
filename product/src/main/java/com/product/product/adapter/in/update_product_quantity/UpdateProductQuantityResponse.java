package com.product.product.adapter.in.update_product_quantity;

import com.product.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class UpdateProductQuantityResponse {

    private String result;

    @Builder
    UpdateProductQuantityResponse(String result) {
        this.result = result;
    }

    UpdateProductQuantityResponse of(UpdateProductQuantityServiceResponse serviceResponse) {
        return UpdateProductQuantityResponse.builder()
            .result(serviceResponse.result())
            .build();
    }
}

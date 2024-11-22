package com.product.product.adapter.in.update_product_quantity;

import com.product.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class UpdateProductQuantityResponse {

    private String resuult;

    @Builder
    UpdateProductQuantityResponse(String resuult) {
        this.resuult = resuult;
    }

    UpdateProductQuantityResponse of(UpdateProductQuantityServiceResponse serviceResponse) {
        return UpdateProductQuantityResponse.builder()
            .resuult(serviceResponse.result())
            .build();
    }
}

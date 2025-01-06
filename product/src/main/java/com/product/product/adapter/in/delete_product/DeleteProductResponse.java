package com.product.product.adapter.in.delete_product;

import com.product.product.application.service.delete_product.DeleteProductServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class DeleteProductResponse {

    String result;

    @Builder
    DeleteProductResponse(String result) {
        this.result = result;
    }

    DeleteProductResponse of(DeleteProductServiceResponse serviceResponse) {
        return DeleteProductResponse.builder()
            .result(serviceResponse.result())
            .build();
    }
}

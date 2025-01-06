package com.product.product.adapter.in.delete_product;

import com.product.global.response.ApiResponse;
import com.product.product.application.port.in.DeleteProductUseCase;
import com.product.product.application.service.delete_product.DeleteProductServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteProductController {

    private final DeleteProductUseCase deleteProductUseCase;

    @DeleteMapping("/products/{id}")
    ApiResponse<DeleteProductResponse> deleteProduct(@PathVariable Long id,
        @RequestHeader(value = "Authorization", required = false) String authorization) {

        DeleteProductServiceResponse serviceResponse = deleteProductUseCase
            .deleteProduct(id, authorization);
        return ApiResponse.ok(new DeleteProductResponse().of(serviceResponse));
    }
}

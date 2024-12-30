package com.product.product.adapter.in.update_product_quantity;

import com.product.global.response.ApiResponse;
import com.product.global.validation.groups.ValidationSequence;
import com.product.product.application.port.in.UpdateProductQuantityUseCase;
import com.product.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UpdateProductQuantityController {

    private final UpdateProductQuantityUseCase updateProductSalesUseCase;

    @PutMapping("/products/{productId}/quantity")
    ApiResponse<UpdateProductQuantityResponse> updateProductQuantity(
        @Validated(ValidationSequence.class) @RequestBody UpdateProductQuantityRequest request,
        @RequestHeader("Authorization") String authentication,
        @PathVariable Long productId) {

        UpdateProductQuantityServiceResponse serviceResponse = updateProductSalesUseCase
            .updateProductQuantity(request.toCommand(productId, authentication));
        return ApiResponse.ok(new UpdateProductQuantityResponse().of(serviceResponse));
    }
}

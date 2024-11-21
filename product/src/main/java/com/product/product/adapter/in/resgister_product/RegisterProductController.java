package com.product.product.adapter.in.resgister_product;

import com.product.global.response.ApiResponse;
import com.product.product.application.port.in.RegisterProductUseCase;
import com.product.product.application.service.register_product.RegisterProductServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterProductController {

    private final RegisterProductUseCase registerProductUseCase;

    @PostMapping("/products")
    ApiResponse<?> registerProduct(@RequestBody @Valid RegisterProductRequest request,
        @RequestHeader("Authorization") String authorization) {
        request.validate();

        RegisterProductServiceResponse serviceResponse = registerProductUseCase
            .registerProduct(request.toCommand(authorization));
        return ApiResponse.ok(new RegisterProductResponse().of(serviceResponse));
    }
}

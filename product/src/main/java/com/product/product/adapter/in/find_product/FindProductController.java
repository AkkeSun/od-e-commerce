package com.product.product.adapter.in.find_product;

import com.product.global.response.ApiResponse;
import com.product.product.application.port.in.FindProductUseCase;
import com.product.product.application.service.find_product.FindProductServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindProductController {

    private final FindProductUseCase findProductUseCase;

    @GetMapping("/products/{productId}")
    ApiResponse<FindProductResponse> findProduct(@PathVariable Long productId) {
        FindProductServiceResponse serviceResponse = findProductUseCase.findProduct(productId);
        return ApiResponse.ok(new FindProductResponse().of(serviceResponse));
    }
}

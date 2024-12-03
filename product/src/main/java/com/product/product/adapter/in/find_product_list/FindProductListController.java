package com.product.product.adapter.in.find_product_list;

import com.product.global.response.ApiResponse;
import com.product.global.validation.groups.ValidationSequence;
import com.product.product.application.port.in.FindProductListUseCase;
import com.product.product.application.service.find_product_list.FindProductListServiceResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindProductListController {

    private final FindProductListUseCase findProductListUseCase;

    @GetMapping("/products")
    ApiResponse<List<FindProductListResponse>> findProductList(
        @Validated(ValidationSequence.class) FindProductListRequest request) {

        List<FindProductListServiceResponse> serviceResponse = findProductListUseCase
            .findProductList(request.toCommand());
        return ApiResponse.ok(serviceResponse.stream().map(response ->
            new FindProductListResponse().of(response)).toList());
    }
}

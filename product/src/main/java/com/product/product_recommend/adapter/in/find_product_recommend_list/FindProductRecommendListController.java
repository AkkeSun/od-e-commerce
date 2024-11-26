package com.product.product_recommend.adapter.in.find_product_recommend_list;

import com.product.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindProductRecommendListController {

    @GetMapping("/recommend-products")
    ApiResponse<List<FindProductRecommendListResponse>> findRecommendProductList(
        @RequestHeader(name = "Authorization", required = false) String authorization,
        String category) {

        return null;
    }
}

package com.product.product.adapter.in.find_product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.product.application.service.find_product.FindProductServiceResponse;
import com.product.product.domain.Category;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

class FindProductControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[findProduct] 상품 정보를 조회하는 API")
    class Describe_findProduct {

        @Test
        @WithAnonymousUser
        @DisplayName("[success] API 를 요청할 때 200코드와 상품 리스트를 응답한다.")
        void success() throws Exception {
            // given
            Long productId= 10L;
            FindProductServiceResponse response = FindProductServiceResponse.builder()
                .productId(productId)
                .sellerId(12L)
                .sellerEmail("test")
                .productName("상품 이름")
                .productImg("상품 이미지")
                .productOption(List.of("옵션1", "옵션2"))
                .description("상품 설명")
                .price(10000)
                .category(Category.BOOKS)
                .reviews(new ArrayList<>())
                .regDateTime("2021-08-01 12:23:12")
                .build();
            given(findProductUseCase.findProduct(any())).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(get("/products/{productId}", productId));

            // then
            actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.productId").value(productId))
                .andExpect(jsonPath("$.data.sellerId").value(12))
                .andExpect(jsonPath("$.data.sellerEmail").value("test"))
                .andExpect(jsonPath("$.data.productName").value("상품 이름"))
                .andExpect(jsonPath("$.data.productImg").value("상품 이미지"))
                .andExpect(jsonPath("$.data.productOption").isArray())
                .andExpect(jsonPath("$.data.description").value("상품 설명"))
                .andExpect(jsonPath("$.data.price").value(10000))
                .andExpect(jsonPath("$.data.category").value("BOOKS"))
                .andExpect(jsonPath("$.data.reviews").isArray())
                .andExpect(jsonPath("$.data.reviews").isEmpty())
                .andExpect(jsonPath("$.data.regDateTime").value("2021-08-01 12:23:12"))
                .andDo(print());

        }
    }
}
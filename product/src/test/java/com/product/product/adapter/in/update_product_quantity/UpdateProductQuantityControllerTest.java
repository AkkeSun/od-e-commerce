package com.product.product.adapter.in.update_product_quantity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.ControllerTestSupport;
import com.product.global.exception.ErrorCode;
import com.product.global.exception.ErrorResponse;
import com.product.global.response.ApiResponse;
import com.product.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class UpdateProductQuantityControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[updateProductQuantity] 상품 수량을 수정하는 API")
    class Describe_updateProductQuantity {

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[success] 인증받은 사용자가 필수값을 모두 입력하여 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .productCount(10)
                .isPurchased(true)
                .build();
            String authorization = "testToken";
            String productId = "10";
            UpdateProductQuantityServiceResponse response = UpdateProductQuantityServiceResponse.builder()
                .result("Y")
                .build();
            given(updateProductSalesUseCase.updateProductQuantity(any())).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}/quantity", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.result").value("Y"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            UpdateProductQuantityRequest request = new UpdateProductQuantityRequest();
            String authorization = "testToken";
            String productId = "10";
            given(jsonUtil.toJsonString(any())).willReturn(
                new ObjectMapper().writeValueAsString(ApiResponse.of(
                    HttpStatus.UNAUTHORIZED,
                    ErrorResponse.builder()
                        .errorCode(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getCode())
                        .errorMessage(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getMessage())
                        .build())));

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}/quantity", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.httpStatus").value(401))
                .andExpect(jsonPath("$.message").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(
                    ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getCode()))
                .andExpect(jsonPath("$.data.errorMessage").value(
                    ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getMessage()))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 인증받은 사용자가 사용자가 상품 수량을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .isPurchased(true)
                .build();
            String authorization = "testToken";
            String productId = "10";

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}/quantity", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("상품 수량은 1 이상 이어야 합니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 인증받은 사용자가 사용자가 1 미만의 상품 수량을 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .productCount(-1)
                .isPurchased(true)
                .build();
            String authorization = "testToken";
            String productId = "10";

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}/quantity", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("상품 수량은 1 이상 이어야 합니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 인증받은 사용자가 사용자가 판매 여부를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .productCount(10)
                .build();
            String authorization = "testToken";
            String productId = "10";

            // when
            ResultActions actions = mockMvc.perform(put("/products/{productId}/quantity", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("판매 여부는 필수값 입니다"))
                .andDo(print());
        }
    }
}
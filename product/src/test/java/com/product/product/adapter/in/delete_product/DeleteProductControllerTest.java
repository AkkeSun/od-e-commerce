package com.product.product.adapter.in.delete_product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.ControllerTestSupport;
import com.product.global.exception.ErrorCode;
import com.product.global.exception.ErrorResponse;
import com.product.global.response.ApiResponse;
import com.product.product.application.service.delete_product.DeleteProductServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class DeleteProductControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[deleteProduct] 상품을 삭제하는 API")
    class Describe_deleteProduct {

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 요청을 보낸 경우 401 코드와 에러 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            Long productId = 10L;
            String authorization = "testToken";
            given(jsonUtil.toJsonString(any())).willReturn(
                new ObjectMapper().writeValueAsString(ApiResponse.of(
                    HttpStatus.UNAUTHORIZED,
                    ErrorResponse.builder()
                        .errorCode(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getCode())
                        .errorMessage(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getMessage())
                        .build())));

            // when
            ResultActions actions = mockMvc.perform(delete("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
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
        @WithMockUser(username = "od", roles = "CUSTOMER")
        @DisplayName("[error] 권한이 없는 사용자가 API 요청을 보낸 경우 403 코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            Long productId = 11L;
            String authorization = "testToken2";
            given(jsonUtil.toJsonString(any())).willReturn(
                new ObjectMapper().writeValueAsString(ApiResponse.of(
                    HttpStatus.FORBIDDEN,
                    ErrorResponse.builder()
                        .errorCode(ErrorCode.ACCESS_DENIED_BY_SECURITY.getCode())
                        .errorMessage(ErrorCode.ACCESS_DENIED_BY_SECURITY.getMessage())
                        .build())));

            // when
            ResultActions actions = mockMvc.perform(delete("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
            );

            // then
            actions.andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.httpStatus").value(403))
                .andExpect(jsonPath("$.message").value("FORBIDDEN"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(
                    ErrorCode.ACCESS_DENIED_BY_SECURITY.getCode()))
                .andExpect(jsonPath("$.data.errorMessage").value(
                    ErrorCode.ACCESS_DENIED_BY_SECURITY.getMessage()))
                .andDo(print());
        }

        @Test
        @WithMockUser(username = "od", roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 API 요청을 보낸 경우 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 12L;
            String authorization = "testToken3";
            given(deleteProductUseCase.deleteProduct(any(), any())).willReturn(
                DeleteProductServiceResponse.builder()
                    .result("Y")
                    .build());

            // when
            ResultActions actions = mockMvc.perform(delete("/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
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
    }
}
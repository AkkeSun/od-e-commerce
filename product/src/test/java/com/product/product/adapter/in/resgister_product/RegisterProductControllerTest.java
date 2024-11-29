package com.product.product.adapter.in.resgister_product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.ControllerTestSupport;
import com.product.global.exception.ErrorCode;
import com.product.global.exception.ErrorResponse;
import com.product.global.response.ApiResponse;
import com.product.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.product.domain.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class RegisterProductControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[registerProduct] 상품 정보를 등록하는 API")
    class Describe_registerProduct {

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[success] 판매 권한을 가진 사용자가 필수값을 모두 입력하여 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            RegisterProductServiceResponse response = RegisterProductServiceResponse.builder()
                .productId(12345L)
                .sellerEmail("od@test.com")
                .productName(request.getProductName())
                .productImg(request.getProductImg())
                .description(request.getDescription())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .category(Category.valueOf(request.getCategory()))
                .build();
            String authorization = "testToken";
            given(registerProductUseCase.registerProduct(any())).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.productId").value(response.productId()))
                .andExpect(jsonPath("$.data.sellerEmail").value(response.sellerEmail()))
                .andExpect(jsonPath("$.data.productName").value(response.productName()))
                .andExpect(jsonPath("$.data.productImg").value(response.productImg()))
                .andExpect(jsonPath("$.data.description").value(response.description()))
                .andExpect(jsonPath("$.data.price").value(response.price()))
                .andExpect(jsonPath("$.data.quantity").value(response.quantity()))
                .andExpect(jsonPath("$.data.category").value(response.category().name()))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            RegisterProductRequest request = new RegisterProductRequest();
            String authorization = "testToken";
            given(jsonUtil.toJsonString(any())).willReturn(
                new ObjectMapper().writeValueAsString(ApiResponse.of(
                    HttpStatus.UNAUTHORIZED,
                    ErrorResponse.builder()
                        .errorCode(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getCode())
                        .errorMessage(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getMessage())
                        .build())));

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 상품 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterProductRequest request = new RegisterProductRequest();
            String authorization = "testToken";
            given(jsonUtil.toJsonString(any())).willReturn(
                new ObjectMapper().writeValueAsString(ApiResponse.of(
                    HttpStatus.FORBIDDEN,
                    ErrorResponse.builder()
                        .errorCode(ErrorCode.ACCESS_DENIED_BY_SECURITY.getCode())
                        .errorMessage(ErrorCode.ACCESS_DENIED_BY_SECURITY.getMessage())
                        .build())));

            // when
            ResultActions actions = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
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
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName(null)
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품명은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 빈 값으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품명은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 이미지를 입력하지 않았을 을 때 400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg(null)
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 이미지는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 이미지를 빈 값으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 이미지는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 설명을 입력하지 않았을 을 때 400코드와 에러 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description(null)
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 설명은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 설명을 빈 값으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error8() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 설명은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 금액을 입력하지 않았을 을 때 400코드와 에러 메시지를 응답한다.")
        void error9() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("금액은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품금액을 1 미만으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error10() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(0)
                .quantity(30)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("금액은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 수량을 입력하지 않았을 을 때 400코드와 에러 메시지를 응답한다.")
        void error11() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 수량은 20 이상이어야 합니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 수량을 20 미만으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error12() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(15)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("상품 수량은 20 이상이어야 합니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 카테고리를 않았을 을 때 400코드와 에러 메시지를 응답한다.")
        void error13() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(50)
                .category(null)
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("카테고리는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 카테고리를 입력하지 않았을때 400코드와 에러 메시지를 응답한다.")
        void error14() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(50)
                .category(null)
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("카테고리는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 유효하지 않은 상품 카테고리를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error15() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(50)
                .category("errorCategory")
                .build();
            String authorization = "testToken";

            // when
            ResultActions actions = mockMvc.perform(post("/products")
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
                .andExpect(jsonPath("$.data.errorMessage").value("유효한 카테고리가 아닙니다"))
                .andDo(print());
        }
    }
}
package com.product.review.adapter.in.register_review;

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
import com.product.review.application.service.register_review.RegisterReviewServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class RegisterReviewControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[registerReview] 리뷰를 등록하는 API")
    class Describe_registerReview {

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[success] 인증받은 사용자가 필수값을 모두 입력하여 API 를 요청했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .score(5)
                .comment("리뷰 내용")
                .build();
            RegisterReviewServiceResponse response = RegisterReviewServiceResponse.builder()
                .id(10L)
                .productId(productId)
                .accountId(12L)
                .score(request.getScore())
                .comment(request.getComment())
                .regDate("20210801")
                .build();

            given(registerReviewUseCase.registerReview(any())).willReturn(response);

            // when
            ResultActions perform = mockMvc.perform(post("/products/{productId}/reviews", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            perform.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.productId").value(productId))
                .andExpect(jsonPath("$.data.accountId").value(response.accountId()))
                .andExpect(jsonPath("$.data.score").value(request.getScore()))
                .andExpect(jsonPath("$.data.comment").value(request.getComment()))
                .andExpect(jsonPath("$.data.regDate").value(response.regDate()));
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .score(5)
                .comment("리뷰 내용")
                .build();
            given(jsonUtil.toJsonString(any())).willReturn(
                new ObjectMapper().writeValueAsString(ApiResponse.of(
                    HttpStatus.UNAUTHORIZED,
                    ErrorResponse.builder()
                        .errorCode(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getCode())
                        .errorMessage(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getMessage())
                        .build())));

            // when
            ResultActions perform = mockMvc.perform(post("/products/{productId}/reviews", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            perform.andExpect(status().isUnauthorized())
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
        @DisplayName("[error] 인증받은 사용자가 평점을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .comment("리뷰 내용")
                .build();

            // when
            ResultActions perform = mockMvc.perform(post("/products/{productId}/reviews", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            perform.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("평점은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 인증받은 사용자가 평점을 1점 미만으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .score(0)
                .comment("리뷰 내용")
                .build();

            // when
            ResultActions perform = mockMvc.perform(post("/products/{productId}/reviews", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            perform.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("평점은 1점 부터 5점까지만 입력 가능합니다"))
                .andDo(print());
        }


        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 인증받은 사용자가 평점을 1점 미만으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .score(6)
                .comment("리뷰 내용")
                .build();

            // when
            ResultActions perform = mockMvc.perform(post("/products/{productId}/reviews", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            perform.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("평점은 1점 부터 5점까지만 입력 가능합니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 인증받은 사용자가 리뷰를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .score(5)
                .build();

            // when
            ResultActions perform = mockMvc.perform(post("/products/{productId}/reviews", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            perform.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("리뷰는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 인증받은 사용자가 리뷰를 빈 값으로 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .score(5)
                .comment("")
                .build();

            // when
            ResultActions perform = mockMvc.perform(post("/products/{productId}/reviews", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            perform.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("리뷰는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 인증받은 사용자가 리뷰 최대값을 초과하여 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .score(5)
                .comment("안녕하세요".repeat(101))
                .build();

            // when
            ResultActions perform = mockMvc.perform(post("/products/{productId}/reviews", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            perform.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("리뷰는 500자를 초과할 수 없습니다"))
                .andDo(print());
        }
    }
}
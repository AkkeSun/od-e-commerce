package com.product.review.adapter.in.register_review;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.product.RestDocsSupport;
import com.product.global.exception.CustomAuthenticationException;
import com.product.global.exception.ErrorCode;
import com.product.review.application.port.in.RegisterReviewUseCase;
import com.product.review.application.service.register_review.RegisterReviewServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultMatcher;

class RegisterReviewDocsTest extends RestDocsSupport {

    RegisterReviewUseCase registerReviewUseCase = mock(RegisterReviewUseCase.class);

    @Override
    protected Object initController() {
        return new RegisterReviewController(registerReviewUseCase);
    }

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

            // when // then
            performSuccess(request, authorization, productId, status().isOk());
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
            given(registerReviewUseCase.registerReview(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when then
            performError(request, authorization, productId, status().isUnauthorized(),
                "[register-review] 인증받지 않은 사용자 접근");
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 인증받은 사용자가 평점 입력 범위를 초과했을 때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            String authorization = "Bearer token";
            Long productId = 1L;
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .score(10)
                .comment("리뷰 내용")
                .build();

            // when then
            performError(request, authorization, productId, status().isBadRequest(),
                "[register-review] 평점 입력 범위 초과");
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
                .comment("")
                .build();

            // when then
            performError(request, authorization, productId, status().isBadRequest(),
                "[register-review] 리뷰 미입력");
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

            // when then
            performError(request, authorization, productId, status().isBadRequest(),
                "[register-review] 리뷰 입력값 초과");
        }

        private void performSuccess(RegisterReviewRequest request, String authorization,
            Long productId, ResultMatcher status)
            throws Exception {

            mockMvc.perform(post("/products/{productId}/reviews", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", authorization)
                )
                .andDo(print())
                .andExpect(status)
                .andDo(document("[register-review] success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Review")
                            .summary("상품 리뷰 등록 API")
                            .description("상품 리뷰를 등록하는 API 입니다. <br><br>"
                                + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                                + "2. 해당 상품을 구매한 사용자만 리뷰를 작성할 수 있습니다. <br>"
                                + "3. 리뷰는 한 상품에 한 개만 등록 가능 합니다. <br>"
                                + "4. 평점, 리뷰는 필수값 입니다. <br>")
                            .requestFields(
                                fieldWithPath("score").type(JsonFieldType.NUMBER)
                                    .description("평점 (1~5)"),
                                fieldWithPath("comment").type(JsonFieldType.STRING)
                                    .description("리뷰 (500자 이하)")
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                    .description("리뷰 아이디"),
                                fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                                    .description("상품 코드"),
                                fieldWithPath("data.accountId").type(JsonFieldType.NUMBER)
                                    .description("사용자 아이디"),
                                fieldWithPath("data.score").type(JsonFieldType.NUMBER)
                                    .description("평점"),
                                fieldWithPath("data.comment").type(JsonFieldType.STRING)
                                    .description("리뷰"),
                                fieldWithPath("data.regDate").type(JsonFieldType.STRING)
                                    .description("등록일")
                            )
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                            .requestSchema(Schema.schema("[request] register-review"))
                            .responseSchema(Schema.schema("[response] register-review"))
                            .build())
                    )
                );
        }

        private void performError(RegisterReviewRequest request, String authorization,
            Long productId, ResultMatcher status, String identifier)
            throws Exception {

            mockMvc.perform(post("/products/{productId}/reviews", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", authorization)
                )
                .andDo(print())
                .andExpect(status)
                .andDo(document(identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Review")
                            .summary("상품 리뷰 등록 API")
                            .description("상품 리뷰를 등록하는 API 입니다. <br><br>"
                                + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                                + "2. 해당 상품을 구매한 사용자만 리뷰를 작성할 수 있습니다. <br>"
                                + "3. 리뷰는 한 상품에 한 개만 등록 가능 합니다. <br>"
                                + "4. 평점, 리뷰는 필수값 입니다. <br>")
                            .requestFields(
                                fieldWithPath("score").type(JsonFieldType.NUMBER)
                                    .description("평점 (1~5)"),
                                fieldWithPath("comment").type(JsonFieldType.STRING)
                                    .description("리뷰 (500자 이하)")
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.errorCode").type(JsonFieldType.NUMBER)
                                    .description("에러 코드"),
                                fieldWithPath("data.errorMessage").type(JsonFieldType.STRING)
                                    .description("에러 메시지")
                            )
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                            .requestSchema(Schema.schema("[request] register-review"))
                            .responseSchema(Schema.schema("[response] error"))
                            .build())
                    )
                );
        }
    }
}
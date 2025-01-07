package com.product.product.adapter.in.delete_product;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
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
import com.product.global.exception.CustomAuthorizationException;
import com.product.global.exception.ErrorCode;
import com.product.product.application.port.in.DeleteProductUseCase;
import com.product.product.application.service.delete_product.DeleteProductServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultMatcher;

class DeleteProductDocsTest extends RestDocsSupport {

    private final DeleteProductUseCase deleteProductUseCase = mock(DeleteProductUseCase.class);

    @Override
    protected Object initController() {
        return new DeleteProductController(deleteProductUseCase);
    }

    @Nested
    @DisplayName("[deleteProduct] 상품을 삭제하는 API")
    class Describe_deleteProduct {

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 요청을 보낸 경우 401 코드와 에러 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            Long productId = 10L;
            String authorization = "Bearer token";
            given(deleteProductUseCase.deleteProduct(any(), any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY));

            // when // then
            performError(productId, authorization, status().isUnauthorized(),
                "[delete-product] 인증받지 않은 사용자 접근");
        }

        @Test
        @WithMockUser(username = "od", roles = "CUSTOMER")
        @DisplayName("[error] 권한이 없는 사용자가 API 요청을 보낸 경우 403 코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            Long productId = 11L;
            String authorization = "Bearer token";
            given(deleteProductUseCase.deleteProduct(any(), any())).willThrow(
                new CustomAuthorizationException(ErrorCode.ACCESS_DENIED_BY_SECURITY));

            // when // then
            performError(productId, authorization, status().isForbidden(),
                "[delete-product] 권한이 없는 사용자 접근");
        }

        @Test
        @WithMockUser(username = "od", roles = "CUSTOMER")
        @DisplayName("[error] 상품을 등록한 사용자와 다른 사용자가 API 요청을 보낸 경우 403 코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            Long productId = 11L;
            String authorization = "Bearer token";
            given(deleteProductUseCase.deleteProduct(any(), any())).willThrow(
                new CustomAuthorizationException(ErrorCode.ACCESS_DENIED));

            // when // then
            performError(productId, authorization, status().isForbidden(),
                "[delete-product] 상품을 등록한 사용자가 아닌 사용자 접근");
        }

        @Test
        @WithMockUser(username = "od", roles = "SELLER")
        @DisplayName("[success] 판매 권한을 가진 사용자가 API 요청을 보낸 경우 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 12L;
            String authorization = "Bearer token";
            given(deleteProductUseCase.deleteProduct(any(), any())).willReturn(
                DeleteProductServiceResponse.builder()
                    .result("Y")
                    .build());

            // when
            performSuccess(productId, authorization, status().isOk());
        }


        private void performSuccess(Long productId, String authorization, ResultMatcher status)
            throws Exception {

            mockMvc.perform(RestDocumentationRequestBuilders
                    .delete("/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", authorization)
                )
                .andDo(print())
                .andExpect(status)
                .andDo(document("[delete-product] success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Product")
                            .summary("상품 삭제 API")
                            .description("상품을 삭제하는 API 입니다. <br><br>"
                                + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                                + "2. 상품을 등록한 사용자만 삭제 가능 합니다. <br>"
                                + "3. 삭제 내역은 히스토리 테이블을 통해 확인 가능 합니다.")
                            .pathParameters(parameterWithName("productId").description("상품 코드"))
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.result").type(JsonFieldType.STRING)
                                    .description("성공 유무")
                            )
                            .requestSchema(Schema.schema("[request] delete-product"))
                            .responseSchema(Schema.schema("[response] delete-product"))
                            .build())
                    )
                );
        }

        private void performError(Long productId, String authorization, ResultMatcher status,
            String identifier) throws Exception {
            mockMvc.perform(RestDocumentationRequestBuilders
                    .delete("/products/{productId}", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", authorization)
                )
                .andDo(print())
                .andExpect(status)
                .andDo(document(identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Product")
                            .summary("상품 삭제 API")
                            .description("상품을 삭제하는 API 입니다. <br><br>"
                                + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                                + "2. 상품을 등록한 사용자만 삭제 가능 합니다. <br>"
                                + "3. 삭제 내역은 히스토리 테이블을 통해 확인 가능 합니다.")
                            .pathParameters(parameterWithName("productId").description("상품 코드"))
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
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
                            .requestSchema(Schema.schema("[request] delete-product"))
                            .responseSchema(Schema.schema("[response] error"))
                            .build())
                    )
                );
        }
    }
}
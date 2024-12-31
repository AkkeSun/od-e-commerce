package com.product.product.adapter.in.update_product_quantity;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
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
import com.product.global.exception.CustomBusinessException;
import com.product.global.exception.CustomNotFoundException;
import com.product.global.exception.ErrorCode;
import com.product.product.application.port.in.UpdateProductQuantityUseCase;
import com.product.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultMatcher;

class UpdateProductQuantityDocsTest extends RestDocsSupport {

    UpdateProductQuantityUseCase updateProductSalesUseCase = mock(
        UpdateProductQuantityUseCase.class);

    @Override
    protected Object initController() {
        return new UpdateProductQuantityController(updateProductSalesUseCase);
    }

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
                .updateType("PURCHASE")
                .build();
            String authorization = "testToken";
            Long productId = 10L;
            UpdateProductQuantityServiceResponse response = UpdateProductQuantityServiceResponse.builder()
                .result("Y")
                .build();
            given(updateProductSalesUseCase.updateProductQuantity(any())).willReturn(response);

            // when // then
            performSuccess(productId, request, authorization, status().isOk());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .productCount(10)
                .updateType("PURCHASE")
                .build();
            String authorization = "testToken";
            Long productId = 10L;
            given(updateProductSalesUseCase.updateProductQuantity(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when // then
            performError(productId, request, authorization, status().isUnauthorized(),
                "[update-product-quantity] 인증받지 않은 사용자 접근");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 인증받은 사용자가 사용자가 상품 수량을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .updateType("PURCHASE")
                .productCount(0)
                .build();
            String authorization = "testToken";
            String productId = "10";

            // when // then
            performError(Long.parseLong(productId), request, authorization, status().isBadRequest(),
                "[update-product-quantity] 상품 수량 미입력 혹은 1 미만 입력");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 인증받은 사용자가 사용자가 수정 타입을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .updateType("")
                .productCount(0)
                .build();
            String authorization = "testToken";
            String productId = "10";

            // when // then
            performError(Long.parseLong(productId), request, authorization, status().isBadRequest(),
                "[update-product-quantity] 수정 타입 미입력");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 인증받은 사용자가 사용자가 유효하지 않은 수정 타입을 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .updateType("error")
                .productCount(0)
                .build();
            String authorization = "testToken";
            String productId = "10";

            // when // then
            performError(Long.parseLong(productId), request, authorization, status().isBadRequest(),
                "[update-product-quantity] 유효하지 않은 수정 타입 입력");
        }


        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 인증받은 사용자가 사용자가 입력한 상품코드가 존재하지 않을 경우 예외를 응답한다.")
        void error3() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .updateType("PURCHASE")
                .productCount(1)
                .build();
            String authorization = "testToken";
            String productId = "99";
            given(updateProductSalesUseCase.updateProductQuantity(any())).willThrow(
                new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));

            // when // then
            performError(Long.parseLong(productId), request, authorization, status().isNotFound(),
                "[update-product-quantity] 조회된 상품 없음");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 구매하고자 하는 상품 수량이 부족한 경우 예외를 응답한다.")
        void error4() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .updateType("PURCHASE")
                .productCount(99)
                .build();
            String authorization = "testToken";
            String productId = "111";
            given(updateProductSalesUseCase.updateProductQuantity(any())).willThrow(
                new CustomBusinessException(ErrorCode.Business_OUT_OF_STOCK));

            // when // then
            performError(Long.parseLong(productId), request, authorization, status().isUnprocessableEntity(),
                "[update-product-quantity] 상품 수량 부족");
        }

        private void performSuccess(Long productId, UpdateProductQuantityRequest request,
            String authorization,
            ResultMatcher status)
            throws Exception {

            mockMvc.perform(RestDocumentationRequestBuilders
                    .put("/products/{productId}/quantity", productId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", authorization)
                )
                .andDo(print())
                .andExpect(status)
                .andDo(document("[update-product-quantity] success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Product")
                            .summary("상품 수량 변경 API")
                            .description("상품 수량을 변경하는 API 입니다. <br><br>"
                                + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                                + "2. 인증받은 사용자만 호출 가능 합니다. <br>"
                                + "3. 상품 수량, 수정 타입은 필수값 입니다. <br>"
                                + "4. 구매 여부가 true 일 경우 상품 수량이 감소하고, false 일 경우 상품 수량이 증가합니다.")
                            .requestFields(
                                fieldWithPath("productCount").type(JsonFieldType.NUMBER)
                                    .description("상품 수량 (1 이상)"),
                                fieldWithPath("updateType").type(JsonFieldType.STRING)
                                    .description(
                                        "수정 타입 (PURCHASE: 구매, REFUND: 환불, ADD_QUANTITY: 상품 수량 증가)")
                            )
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
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                            .requestSchema(Schema.schema("[request] update-product-quantity"))
                            .responseSchema(Schema.schema("[response] update-product-quantity"))
                            .build())
                    )
                );
        }

        private void performError(Long productId, UpdateProductQuantityRequest request,
            String authorization, ResultMatcher status, String identifier)
            throws Exception {

            mockMvc.perform(RestDocumentationRequestBuilders
                    .put("/products/{productId}/quantity", productId)
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
                            .tag("Product")
                            .summary("상품 수량 변경 API")
                            .description("상품 수량을 변경하는 API 입니다. <br><br>"
                                + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                                + "2. 인증받은 사용자만 호출 가능 합니다. <br>"
                                + "3. 상품 수량, 수정 타입은 필수값 입니다. <br>"
                                + "4. 구매 여부가 true 일 경우 상품 수량이 감소하고, false 일 경우 상품 수량이 증가합니다.")
                            .requestFields(
                                fieldWithPath("productCount").type(JsonFieldType.NUMBER)
                                    .description("상품 수량 (1 이상)"),
                                fieldWithPath("updateType").type(JsonFieldType.STRING)
                                    .description(
                                        "수정 타입 (PURCHASE: 구매, REFUND: 환불, ADD_QUANTITY: 상품 수량 증가)")
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
                            .requestSchema(Schema.schema("[request] update-product-quantity"))
                            .responseSchema(Schema.schema("[response] error"))
                            .build())
                    )
                );
        }
    }
}
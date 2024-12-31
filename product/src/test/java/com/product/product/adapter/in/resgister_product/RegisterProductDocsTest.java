package com.product.product.adapter.in.resgister_product;

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
import com.product.global.exception.CustomAuthorizationException;
import com.product.global.exception.ErrorCode;
import com.product.product.application.port.in.RegisterProductUseCase;
import com.product.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.product.domain.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultMatcher;

class RegisterProductDocsTest extends RestDocsSupport {

    RegisterProductUseCase registerProductUseCase = mock(RegisterProductUseCase.class);

    @Override
    protected Object initController() {
        return new RegisterProductController(registerProductUseCase);
    }

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
            performSuccess(request, authorization, status().isOk());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
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
            String authorization = "testToken";
            given(registerProductUseCase.registerProduct(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when // then
            performError(request, authorization, status().isUnauthorized(),
                "[find-product-list] 인증받지 않은 사용자 접근");
        }

        @Test
        @WithMockUser(roles = "CUSTOMER")
        @DisplayName("[error] 상품 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
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
            String authorization = "testToken";
            given(registerProductUseCase.registerProduct(any())).willThrow(
                new CustomAuthorizationException(ErrorCode.ACCESS_DENIED));

            // when // then
            performError(request, authorization, status().isForbidden(),
                "[find-product-list] 상품 등록 권한이 없는 사용자 접근");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
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

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 상품명 미입력");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 이미지를 입력하지 않았을 을 때 400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
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

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 상품 이미지 미입력");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 설명을 입력하지 않았을 을 때 400코드와 에러 메시지를 응답한다.")
        void error7() throws Exception {
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

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 상품 설명 미입력");
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
                .price(0)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 상품 금액 미입력 혹은 1미만 입력");
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
                .quantity(0)
                .price(10000)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 상품 수량 미입력 혹은 20미만 입력");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 카테고리를 입력하지 않았을 을 때 400코드와 에러 메시지를 응답한다.")
        void error13() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("상품 이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(50)
                .category("")
                .build();
            String authorization = "testToken";

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 카테고리 미입력");
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

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 유효하지 않은 카테고리 입력");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 이미지를 50자를 초과하여 입력했을 때400코드와 에러 메시지를 응답한다.")
        void error16() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("이미지오류이미지오류이미지오류이미지오류이미지오류이미지오류이미지오류이미지오류이미지오류이미지오류이미지오류")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(50)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 상품 이미지 입력 사이즈 초과");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 50자를 초과하여 입력했을 때400코드와 에러 메시지를 응답한다.")
        void error17() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명오류".repeat(11))
                .productImg("이미지")
                .description("상품 설명")
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(50)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 상품명 입력 사이즈 초과");
        }

        @Test
        @WithMockUser(roles = "SELLER")
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 설명 최대 글자를 초과하여 입력했을 때400코드와 에러 메시지를 응답한다.")
        void error18() throws Exception {
            // given
            String hangul = "안녕하세요"; // 15 byte
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .productImg("이미지")
                .description(String.valueOf(hangul.repeat(4370)))
                .productOption(List.of("옵션1", "옵션2"))
                .price(10000)
                .quantity(50)
                .category("AUTOMOTIVE")
                .build();
            String authorization = "testToken";

            // when // then
            performError(request, authorization, status().isBadRequest(),
                "[find-product-list] 상품 설명 입력 사이즈 초과");
        }

        private void performSuccess(RegisterProductRequest request, String authorization,
            ResultMatcher status)
            throws Exception {

            mockMvc.perform(RestDocumentationRequestBuilders.post("/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", authorization)
                )
                .andDo(print())
                .andExpect(status)
                .andDo(document("[register-product-list] success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Product")
                            .summary("상품 등록 API")
                            .description("상품을 등록하는 API 입니다. <br><br>"
                                + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                                + "2. 판매자 권한을 가진 사용자만 상품을 등록할 수 있습니다. <br>"
                                + "2. 상품명, 상품 이미지, 상품 설명, 금액, 수량, 카테고리는 필수값 입니다. <br>")
                            .requestFields(
                                fieldWithPath("productName").type(JsonFieldType.STRING)
                                    .description("상품명 (50자 이하)"),
                                fieldWithPath("productImg").type(JsonFieldType.STRING)
                                    .description("상품 이미지 (50자 이하)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                    .description("상품 설명 (65535 byte 이하)"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                    .description("금액 (1 이상)"),
                                fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                    .description("수량 (20 이상)"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                    .description(
                                        "카테고리 (ELECTRONICS, FASHION, DIGITAL, HOME_APPLIANCES, BEAUTY, BOOKS, SPORTS, FOOD, TOYS, FURNITURE, AUTOMOTIVE, HEALTH, TOTAL)"),
                                fieldWithPath("productOption").type(JsonFieldType.ARRAY)
                                    .description("상품 옵션").optional()
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                                    .description("상품 코드"),
                                fieldWithPath("data.productName").type(JsonFieldType.STRING)
                                    .description("상품명"),
                                fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                                    .description("가격"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING)
                                    .description("카테고리"),
                                fieldWithPath("data.productImg").type(JsonFieldType.STRING)
                                    .description("상품 이미지"),
                                fieldWithPath("data.sellerEmail").type(JsonFieldType.STRING)
                                    .description("판매자 이메일"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING)
                                    .description("상품 설명"),
                                fieldWithPath("data.quantity").type(JsonFieldType.NUMBER)
                                    .description("수량")
                            )
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                            .requestSchema(Schema.schema("[request] register-product"))
                            .responseSchema(Schema.schema("[response] register-product"))
                            .build())
                    )
                );
        }

        private void performError(RegisterProductRequest request, String authorization,
            ResultMatcher status, String identifier)
            throws Exception {

            mockMvc.perform(RestDocumentationRequestBuilders.post("/products")
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
                            .summary("상품 등록 API")
                            .description("상품을 등록하는 API 입니다. <br><br>"
                                + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                                + "2. 판매자 권한을 가진 사용자만 상품을 등록할 수 있습니다. <br>"
                                + "2. 상품명, 상품 이미지, 상품 설명, 금액, 수량, 카테고리는 필수값 입니다. <br>")
                            .requestFields(
                                fieldWithPath("productName").type(JsonFieldType.STRING)
                                    .description("상품명 (50자 이하)"),
                                fieldWithPath("productImg").type(JsonFieldType.STRING)
                                    .description("상품 이미지 (50자 이하)"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                    .description("상품 설명 (65535 byte 이하)"),
                                fieldWithPath("price").type(JsonFieldType.NUMBER)
                                    .description("금액 (1 이상)"),
                                fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                    .description("수량 (20 이상)"),
                                fieldWithPath("category").type(JsonFieldType.STRING)
                                    .description(
                                        "카테고리 (ELECTRONICS, FASHION, DIGITAL, HOME_APPLIANCES, BEAUTY, BOOKS, SPORTS, FOOD, TOYS, FURNITURE, AUTOMOTIVE, HEALTH, TOTAL)"),
                                fieldWithPath("productOption").type(JsonFieldType.ARRAY)
                                    .description("상품 옵션").optional()
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
                            .requestSchema(Schema.schema("[request] register-product"))
                            .responseSchema(Schema.schema("[response] error"))
                            .build())
                    )
                );
        }
    }

}
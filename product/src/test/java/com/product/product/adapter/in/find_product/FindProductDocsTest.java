package com.product.product.adapter.in.find_product;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.product.RestDocsSupport;
import com.product.global.exception.CustomNotFoundException;
import com.product.global.exception.ErrorCode;
import com.product.product.application.port.in.FindProductUseCase;
import com.product.product.application.service.find_product.FindProductServiceResponse;
import com.product.product.domain.Category;
import com.product.review.domain.Review;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultMatcher;

class FindProductDocsTest extends RestDocsSupport {

    private FindProductUseCase findProductUseCase = mock(FindProductUseCase.class);

    @Override
    protected Object initController() {
        return new FindProductController(findProductUseCase);
    }

    @Nested
    @DisplayName("[findProduct] 상품 정보를 조회하는 API")
    class Describe_findProduct {

        @Test
        @WithAnonymousUser
        @DisplayName("[success] API 를 요청할 때 200코드와 상품 리스트를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 10L;
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
                .reviews(Collections.singletonList(Review.builder()
                    .accountId(16L)
                    .score(5)
                    .comment("좋아요")
                    .regDateTime(LocalDateTime.now())
                    .build()))
                .regDateTime(LocalDateTime.now().minusMinutes(2)
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
            given(findProductUseCase.findProduct(any())).willReturn(response);

            // when // then
            performSuccess(productId, status().isOk());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 조회된 상품이 없는 경우 예외를 응답한다.")
        void error() throws Exception {
            // given
            Long productId = 10L;
            given(findProductUseCase.findProduct(any())).willThrow(
                new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));

            // when // then
            performError(productId, status().isNotFound(), "[find-product] 조회된 상품 없음");
        }

        private void performSuccess(Long productId, ResultMatcher status) throws Exception {
            mockMvc.perform(
                    RestDocumentationRequestBuilders.get("/products/{productId}", productId))
                .andDo(print())
                .andExpect(status)
                .andDo(document("[find-product] success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Product")
                            .summary("상품 정보 조회 API")
                            .description("상품 정보를 조회하는 API 입니다. <br>"
                                + "리뷰는 최대 10개 까지만 응답 됩니다.")
                            .pathParameters(parameterWithName("productId").description("상품 코드"))
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                                    .description("상품 코드"),
                                fieldWithPath("data.sellerId").type(JsonFieldType.NUMBER)
                                    .description("판매자 아이디"),
                                fieldWithPath("data.sellerEmail").type(JsonFieldType.STRING)
                                    .description("판매자 이메일"),
                                fieldWithPath("data.productName").type(JsonFieldType.STRING)
                                    .description("상품명"),
                                fieldWithPath("data.productImg").type(JsonFieldType.STRING)
                                    .description("이미지"),
                                fieldWithPath("data.productOption").type(JsonFieldType.ARRAY)
                                    .description("옵션"),
                                fieldWithPath("data.description").type(JsonFieldType.STRING)
                                    .description("설명"),
                                fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                                    .description("가격"),
                                fieldWithPath("data.category").type(JsonFieldType.STRING)
                                    .description("카테고리"),
                                fieldWithPath("data.reviews").type(JsonFieldType.ARRAY)
                                    .description("리뷰 리스트"),
                                fieldWithPath("data.reviews[].writerId").type(JsonFieldType.NUMBER)
                                    .description("리뷰 작성자 아이디"),
                                fieldWithPath("data.reviews[].score").type(JsonFieldType.NUMBER)
                                    .description("리뷰 점수"),
                                fieldWithPath("data.reviews[].comment").type(JsonFieldType.STRING)
                                    .description("리뷰 내용"),
                                fieldWithPath("data.reviews[].regDateTime").type(JsonFieldType.STRING)
                                    .description("리뷰 작성일시"),
                                fieldWithPath("data.regDateTime").type(JsonFieldType.STRING)
                                    .description("등록일시")
                            )
                            .responseSchema(Schema.schema("[response] find-product"))
                            .build())
                    )
                );
        }

        private void performError(Long productId, ResultMatcher status, String identifier)
            throws Exception {
            mockMvc.perform(
                    RestDocumentationRequestBuilders.get("/products/{productId}", productId))
                .andDo(print())
                .andExpect(status)
                .andDo(document(identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Product")
                            .summary("상품 정보 조회 API")
                            .description("상품 정보를 조회하는 API 입니다. <br>"
                                + "리뷰는 최대 10개 까지만 응답 됩니다.")
                            .pathParameters(parameterWithName("productId").description("상품 코드"))
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
                            .responseSchema(Schema.schema("[response] error"))
                            .build())
                    )
                );
        }
    }
}
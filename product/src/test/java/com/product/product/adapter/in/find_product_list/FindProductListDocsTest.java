package com.product.product.adapter.in.find_product_list;

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
import com.product.product.application.port.in.FindProductListUseCase;
import com.product.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.product.domain.Category;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultMatcher;

class FindProductListDocsTest extends RestDocsSupport {

    FindProductListUseCase findProductListUseCase = mock(FindProductListUseCase.class);

    @Override
    protected Object initController() {
        return new FindProductListController(findProductListUseCase);
    }

    @Nested
    @DisplayName("[findProductList] 상품 리스트를 조회하는 API")
    class Describe_findProductList {

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 필수값을 모두 입력하여 API 를 요청 하였고 조회된 상품 리스트가 있을 때 200코드와 상품 리스트를 응답한다.")
        void success() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword("겨울 코트")
                .sortType("RECOMMENDED")
                .page(0)
                .category("FASHION")
                .excludeProductIds(List.of(1L, 2L))
                .build();
            List<FindProductListServiceResponse> response = Arrays.asList(
                FindProductListServiceResponse.builder()
                    .productId(3L)
                    .productName("멋진 겨울 코트")
                    .productImg("겨울 코트 이미지")
                    .sellerEmail("od@gmail.com")
                    .category(Category.FASHION)
                    .price(10000)
                    .regDateTime("2024-12-31 12:22:23")
                    .build(),
                FindProductListServiceResponse.builder()
                    .productId(4L)
                    .productName("겨울 코트")
                    .productImg("겨울 코트 이미지2")
                    .sellerEmail("exg@gmail.com")
                    .category(Category.FASHION)
                    .price(20000)
                    .regDateTime("2024-12-20 12:22:23")
                    .build()
            );
            given(findProductListUseCase.findProductList(any())).willReturn(response);

            //when //then
            performSuccess(request, status().isOk());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 검색어를 입력하지 않았을 떄 400코드와 에러 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword("")
                .sortType("RECOMMENDED")
                .page(0)
                .category("FASHION")
                .excludeProductIds(List.of(1L, 2L))
                .build();
            given(findProductListUseCase.findProductList(any())).willReturn(
                Collections.emptyList());

            // when
            performError(request, status().isBadRequest(), "[find-product-list] 검색어 미입력");
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 정렬 타입을 입력하지 않았을 떄 400코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword("겨울 코트")
                .sortType("")
                .page(0)
                .category("FASHION")
                .excludeProductIds(List.of(1L, 2L))
                .build();
            given(findProductListUseCase.findProductList(any())).willReturn(
                Collections.emptyList());

            //when //then
            performError(request, status().isBadRequest(), "[find-product-list] 정렬타입 미입력");
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 유효하지 않는 정렬 타입을 입력 했을때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword("겨울 코트")
                .sortType("error")
                .page(0)
                .category("FASHION")
                .excludeProductIds(List.of(1L, 2L))
                .build();
            given(findProductListUseCase.findProductList(any())).willReturn(
                Collections.emptyList());

            //when //then
            performError(request, status().isBadRequest(), "[find-product-list] 유효하지 않는 정렬 타입 입력");
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 유효하지 않는 카테고리를 입력 했을때 400코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword("겨울 코트")
                .sortType("RECOMMENDED")
                .page(0)
                .category("error")
                .excludeProductIds(List.of(1L, 2L))
                .build();
            given(findProductListUseCase.findProductList(any())).willReturn(
                Collections.emptyList());

            //when //then
            performError(request, status().isBadRequest(), "[find-product-list] 유효하지 않는 카테고리 입력");
        }

        private void performSuccess(FindProductListRequest request,
            ResultMatcher status)
            throws Exception {

            mockMvc.perform(RestDocumentationRequestBuilders.get("/products")
                    .param("keyword", request.getKeyword())
                    .param("sortType", request.getSortType())
                    .param("page", String.valueOf(request.getPage()))
                    .param("category", request.getCategory())
                    .param("excludeProductIds", request.getExcludeProductIds().stream()
                        .map(String::valueOf)
                        .toArray(String[]::new))
                )
                .andDo(print())
                .andExpect(status)
                .andDo(document("[find-product-list] success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Product")
                            .summary("상품 리스트 조회 API")
                            .description("키워드 기반 상품 리스트를 조회하는 API 입니다. <br><br>"
                                + "1. 입력한 키워드의 캐시 정보가 있다면 캐시 정보를 응답합니다. <br>"
                                + "2. 입력한 키워드의 캐시 정보가 없다면 엘라스틱 서치에서 키워드를 조회합니다. <br>"
                                + "3. 조회된 상품 리스트가 responsePageSize 보다 적다면 벡터 서치를 통해 상품 리스트를 보충합니다. <br>")
                            .queryParameters(
                                parameterWithName("keyword").description("키워드"),
                                parameterWithName("sortType").description(
                                    "정렬 타입 (RECOMMENDED, LOWEST_PRICE, HIGHEST_PRICE, SALES_VOLUME, LATEST, MOST_REVIEWS)"),
                                parameterWithName("category").description(
                                        "카테고리 (ELECTRONICS, FASHION, DIGITAL, HOME_APPLIANCES, BEAUTY, BOOKS, SPORTS, FOOD, TOYS, FURNITURE, AUTOMOTIVE, HEALTH, TOTAL)")
                                    .optional(),
                                parameterWithName("page").description("조회 페이지 (0부터 시작)").optional(),
                                parameterWithName("excludeProductIds").description("제외할 상품 ID 리스트")
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.ARRAY)
                                    .description("응답 데이터"),
                                fieldWithPath("data[].productId").type(JsonFieldType.NUMBER)
                                    .description("상품 코드"),
                                fieldWithPath("data[].productName").type(JsonFieldType.STRING)
                                    .description("상품명"),
                                fieldWithPath("data[].price").type(JsonFieldType.NUMBER)
                                    .description("가격"),
                                fieldWithPath("data[].category").type(JsonFieldType.STRING)
                                    .description("카테고리"),
                                fieldWithPath("data[].productImg").type(JsonFieldType.STRING)
                                    .description("상품 이미지"),
                                fieldWithPath("data[].sellerEmail").type(JsonFieldType.STRING)
                                    .description("판매자 이메일"),
                                fieldWithPath("data[].regDateTime").type(JsonFieldType.STRING)
                                    .description("등록일시")
                            )
                            .responseSchema(Schema.schema("[response] find-product-list"))
                            .build())
                    )
                );
        }

        private void performError(FindProductListRequest request,
            ResultMatcher status, String identifier)
            throws Exception {

            mockMvc.perform(RestDocumentationRequestBuilders.get("/products")
                    .param("keyword", request.getKeyword())
                    .param("sortType", request.getSortType())
                    .param("page", String.valueOf(request.getPage()))
                    .param("category", request.getCategory())
                    .param("excludeProductIds", request.getExcludeProductIds().stream()
                        .map(String::valueOf)
                        .toArray(String[]::new))
                )
                .andDo(print())
                .andExpect(status)
                .andDo(document(identifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Product")
                            .summary("상품 리스트 조회 API")
                            .description("키워드 기반 상품 리스트를 조회하는 API 입니다. <br><br>"
                                + "1. 입력한 키워드의 캐시 정보가 있다면 캐시 정보를 응답합니다. <br>"
                                + "2. 입력한 키워드의 캐시 정보가 없다면 엘라스틱 서치에서 키워드를 조회합니다. <br>"
                                + "3. 조회된 상품 리스트가 responsePageSize 보다 적다면 벡터 서치를 통해 상품 리스트를 보충합니다. <br>")
                            .queryParameters(
                                parameterWithName("keyword").description("키워드"),
                                parameterWithName("sortType").description(
                                    "정렬 타입 (RECOMMENDED, LOWEST_PRICE, HIGHEST_PRICE, SALES_VOLUME, LATEST, MOST_REVIEWS)"),
                                parameterWithName("category").description(
                                        "카테고리 (ELECTRONICS, FASHION, DIGITAL, HOME_APPLIANCES, BEAUTY, BOOKS, SPORTS, FOOD, TOYS, FURNITURE, AUTOMOTIVE, HEALTH, TOTAL)")
                                    .optional(),
                                parameterWithName("page").description("조회 페이지 (0부터 시작)").optional(),
                                parameterWithName("excludeProductIds").description("제외할 상품 ID 리스트")
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
                            .responseSchema(Schema.schema("[response] error"))
                            .build())
                    )
                );
        }
    }
}
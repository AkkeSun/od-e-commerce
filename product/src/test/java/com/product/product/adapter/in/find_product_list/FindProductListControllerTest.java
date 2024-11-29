package com.product.product.adapter.in.find_product_list;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.product.domain.Category;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

class FindProductListControllerTest extends ControllerTestSupport {

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
                    .regDateTime(LocalDateTime.now())
                    .build(),
                FindProductListServiceResponse.builder()
                    .productId(4L)
                    .productName("겨울 코트")
                    .productImg("겨울 코트 이미지2")
                    .sellerEmail("exg@gmail.com")
                    .category(Category.FASHION)
                    .price(20000)
                    .regDateTime(LocalDateTime.now())
                    .build()
            );
            given(findProductListUseCase.findProductList(any())).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("keyword", request.getKeyword())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("category", request.getCategory()));

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data[0].productId").value(3L))
                .andExpect(jsonPath("$.data[0].productName").value("멋진 겨울 코트"))
                .andExpect(jsonPath("$.data[0].productImg").value("겨울 코트 이미지"))
                .andExpect(jsonPath("$.data[0].sellerEmail").value("od@gmail.com"))
                .andExpect(jsonPath("$.data[0].category").value("FASHION"))
                .andExpect(jsonPath("$.data[0].price").value(10000))
                .andExpect(jsonPath("$.data[1].productId").value(4L))
                .andExpect(jsonPath("$.data[1].productName").value("겨울 코트"))
                .andExpect(jsonPath("$.data[1].productImg").value("겨울 코트 이미지2"))
                .andExpect(jsonPath("$.data[1].sellerEmail").value("exg@gmail.com"))
                .andExpect(jsonPath("$.data[1].category").value("FASHION"))
                .andExpect(jsonPath("$.data[1].price").value(20000))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 필수값을 모두 입력하여 API 를 요청 하였고 조회된 상품 리스트가 없을때 200코드와 빈 리스트를 응답한다.")
        void success2() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword("겨울 코트")
                .sortType("RECOMMENDED")
                .page(0)
                .category("FASHION")
                .excludeProductIds(List.of(1L, 2L))
                .build();
            given(findProductListUseCase.findProductList(any())).willReturn(
                Collections.emptyList());

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("keyword", request.getKeyword())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("category", request.getCategory()));

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 검색어를 입력하지 않았을 떄 400코드와 에러 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword(null)
                .sortType("RECOMMENDED")
                .page(0)
                .category("FASHION")
                .excludeProductIds(List.of(1L, 2L))
                .build();
            given(findProductListUseCase.findProductList(any())).willReturn(
                Collections.emptyList());

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("keyword", request.getKeyword())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("category", request.getCategory()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("검색어는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 검색어를 빈 값으로 입력 했을떄 400코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
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
            ResultActions actions = mockMvc.perform(get("/products")
                .param("keyword", request.getKeyword())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("category", request.getCategory()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("검색어는 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 정렬 타입을 입력하지 않았을 떄 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword("겨울 코트")
                .sortType(null)
                .page(0)
                .category("FASHION")
                .excludeProductIds(List.of(1L, 2L))
                .build();
            given(findProductListUseCase.findProductList(any())).willReturn(
                Collections.emptyList());

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("keyword", request.getKeyword())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("category", request.getCategory()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("정렬 타입은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 정렬 타입을 빈 값으로 입력 했을떄 400코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
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

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("keyword", request.getKeyword())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("category", request.getCategory()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("정렬 타입은 필수값 입니다"))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 유효하지 않는 정렬 타입을 입력 했을때 400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
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

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("keyword", request.getKeyword())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("category", request.getCategory()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("유효하지 않은 정렬 타입 입니다"))
                .andDo(print());
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

            // when
            ResultActions actions = mockMvc.perform(get("/products")
                .param("keyword", request.getKeyword())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("category", request.getCategory()));

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("존재하지 않은 카테고리 입니다"))
                .andDo(print());
        }
    }
}
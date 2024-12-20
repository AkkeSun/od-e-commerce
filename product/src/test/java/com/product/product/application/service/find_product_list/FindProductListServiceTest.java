package com.product.product.application.service.find_product_list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.product.IntegrationTestSupport;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.FindProductVectorPort;
import com.product.product.application.port.out.ProduceProductPort;
import com.product.product.application.port.out.RegisterProductEsPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import com.product.product.domain.ProductSortType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;

class FindProductListServiceTest extends IntegrationTestSupport {

    @Autowired
    FindProductListService service;

    @Autowired
    RegisterProductEsPort registerProductEsPort;

    @MockBean
    FindProductVectorPort findProductVectorPort;

    @MockBean
    ProduceProductPort produceProductPort;

    @Nested
    @DisplayName("[findProductList] 상품 리스트를 조회하는 메소드")
    class Describe_findProductList {

        @Test
        @DisplayName("[success] 검색 페이지가 0인 경우 키워드 검색 로그 저장을 위한 메시지를 전송하는지 확인한다.")
        void success1() {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("test")
                .page(0)
                .build();

            // when
            try {
                service.findProductList(command);
            } catch (Exception ignore) {
            }

            // then
            verify(produceProductPort, times(1))
                .sendMessage(any(), any());
        }

        @Test
        @DisplayName("[success] 캐시 데이터가 있는 경우 캐시 데이터를 반환하는지 확인한다.")
        void success2(CapturedOutput output) {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .page(0)
                .build();
            List<FindProductListServiceResponse> cacheList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                cacheList.add(FindProductListServiceResponse.builder()
                    .productId((long) i)
                    .productName("테스트 상품" + i)
                    .price(10000)
                    .category(Category.BEAUTY)
                    .productImg("test.jpg")
                    .sellerEmail("od@gmai.com")
                    .regDateTime(LocalDateTime.now())
                    .build());
            }

            String redisKey = String.format("product-list::%s-%s-%s",
                command.keyword().replace(" ", "-"),
                command.sortType(),
                command.category());
            redisTemplate.opsForValue().set(redisKey, jsonUtil.toJsonString(cacheList));

            // when
            List<FindProductListServiceResponse> result = service.findProductList(command);

            // then
            assertThat(output).contains("[findProductList] response from cache");
            for (FindProductListServiceResponse res : result) {
                assertThat(res.productName()).contains("테스트 상품");
                assertThat(res.price()).isEqualTo(10000);
                assertThat(res.category()).isEqualTo(Category.BEAUTY);
                assertThat(res.productImg()).isEqualTo("test.jpg");
                assertThat(res.sellerEmail()).isEqualTo("od@gmai.com");
            }

            // clean
            redisTemplate.delete(redisKey);
        }

        @Test
        @DisplayName("[success] 캐시 데이터가 없는 경우 엘라스틱서치에서 데이터를 조회하는지 확인한다.")
        void success3(CapturedOutput output) throws InterruptedException {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .build();
            for (int i = 1; i <= 10; i++) {
                registerProductEsPort.register(
                    Product.builder()
                        .productId((long) i)
                        .productName("테스트 상품" + i)
                        .price(10000)
                        .description("테스트 상품 입니다")
                        .category(Category.FASHION)
                        .embeddingYn("Y")
                        .productOption(new ArrayList<>())
                        .price(11000)
                        .quantity(53)
                        .reviewCount(1)
                        .reviewScore(4)
                        .totalScore(5)
                        .hitCount(6)
                        .sellerEmail("121212")
                        .regDate(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .regDateTime(LocalDateTime.now())
                        .build());
            }
            Thread.sleep(1000);

            // when
            List<FindProductListServiceResponse> result = service.findProductList(command);

            // then
            assertThat(output).doesNotContain("[findProductList] response from cache");
            assertThat(output).doesNotContain("[findProductList] response from vector");
            assertThat(result.size()).isEqualTo(10);
            for (FindProductListServiceResponse res : result) {
                assertThat(res.productName()).contains("테스트 상품");
                assertThat(res.price()).isEqualTo(11000);
                assertThat(res.category()).isEqualTo(Category.FASHION);
                assertThat(res.productImg()).isNull();
                assertThat(res.sellerEmail()).isEqualTo("121212");
            }

            // clean
            resetProductEsIndex.resetIndex();

            Thread.sleep(1000);
        }

        @Test
        @DisplayName("[success] 엘라스틱서치에서 조회한 데이터가 응답 데이터 사이즈보다 작다면 벡터 DB 에서 추가 조회를 하고 중복 상품을 제거한 후 반환하는지 확인한다.")
        void success4(CapturedOutput output) throws InterruptedException {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .build();
            for (int i = 1; i <= 8; i++) {
                registerProductEsPort.register(
                    Product.builder()
                        .productId((long) i)
                        .productName("테스트 상품" + i)
                        .price(10000)
                        .description("테스트 상품 입니다")
                        .category(Category.FASHION)
                        .embeddingYn("Y")
                        .productOption(new ArrayList<>())
                        .price(11000)
                        .quantity(53)
                        .reviewCount(1)
                        .reviewScore(4)
                        .totalScore(5)
                        .hitCount(6)
                        .sellerEmail("121212")
                        .regDate(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .regDateTime(LocalDateTime.now())
                        .build());
            }
            LinkedHashSet<Product> vectorList = new LinkedHashSet<>();
            for (int i = 7; i < 20; i++) {
                vectorList.add(Product.builder()
                    .productId((long) i)
                    .productName("테스트 상품" + i)
                    .price(10000)
                    .description("테스트 상품 입니다")
                    .category(Category.FASHION)
                    .embeddingYn("Y")
                    .productOption(new ArrayList<>())
                    .price(11000)
                    .quantity(53)
                    .reviewCount(1)
                    .reviewScore(4)
                    .totalScore(5)
                    .hitCount(6)
                    .sellerEmail("121212")
                    .regDate(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .regDateTime(LocalDateTime.now())
                    .build());
            }
            when(findProductVectorPort.findProductList(command)).thenReturn(vectorList);
            Thread.sleep(1000);

            // when
            List<FindProductListServiceResponse> result = service.findProductList(command);

            // then
            assertThat(output).doesNotContain("[findProductList] response from cache");
            assertThat(output).contains("[findProductList] response from vector");
            assertThat(result.size()).isEqualTo(10);
            for (FindProductListServiceResponse res : result) {
                assertThat(res.productName()).contains("테스트 상품");
                assertThat(res.price()).isEqualTo(11000);
                assertThat(res.category()).isEqualTo(Category.FASHION);
                assertThat(res.productImg()).isNull();
                assertThat(res.sellerEmail()).isEqualTo("121212");
            }

            // clean
            resetProductEsIndex.resetIndex();
            Thread.sleep(1000);
        }

        @Test
        @DisplayName("[success] 벡터 DB 에서 추가 조회를 하는 경우 제외 상품 ID 를 제외한 상품을 반환하는지 확인한다.")
        void success5(CapturedOutput output) throws InterruptedException {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .excludeProductIds(List.of(10L))
                .build();
            for (int i = 1; i <= 8; i++) {
                registerProductEsPort.register(
                    Product.builder()
                        .productId((long) i)
                        .productName("테스트 상품" + i)
                        .price(10000)
                        .description("테스트 상품 입니다")
                        .category(Category.FASHION)
                        .embeddingYn("Y")
                        .productOption(new ArrayList<>())
                        .price(11000)
                        .quantity(53)
                        .reviewCount(1)
                        .reviewScore(4)
                        .totalScore(5)
                        .hitCount(6)
                        .sellerEmail("121212")
                        .regDate(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                        .regDateTime(LocalDateTime.now())
                        .build());
            }
            LinkedHashSet<Product> vectorList = new LinkedHashSet<>();
            for (int i = 7; i < 15; i++) {
                vectorList.add(Product.builder()
                    .productId((long) i)
                    .productName("테스트 상품" + i)
                    .price(10000)
                    .description("테스트 상품 입니다")
                    .category(Category.FASHION)
                    .embeddingYn("Y")
                    .productOption(new ArrayList<>())
                    .price(11000)
                    .quantity(53)
                    .reviewCount(1)
                    .reviewScore(4)
                    .totalScore(5)
                    .hitCount(6)
                    .sellerEmail("121212")
                    .regDate(
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .regDateTime(LocalDateTime.now())
                    .build());
            }
            when(findProductVectorPort.findProductList(command)).thenReturn(vectorList);
            Thread.sleep(1000);

            // when
            List<FindProductListServiceResponse> result = service.findProductList(command);

            // then
            assertThat(output).doesNotContain("[findProductList] response from cache");
            assertThat(output).contains("[findProductList] response from vector");
            assertThat(result.size()).isEqualTo(10);
            for (FindProductListServiceResponse res : result) {
                assertThat(res.productName()).contains("테스트 상품");
                assertThat(res.price()).isEqualTo(11000);
                assertThat(res.category()).isEqualTo(Category.FASHION);
                assertThat(res.productImg()).isNull();
                assertThat(res.sellerEmail()).isEqualTo("121212");
                assertThat(res.productId()).isNotEqualTo(10L);
            }

        }
    }

    @Nested
    @DisplayName("[findCache] 캐시 데이터를 조회하고 슬라이싱 하는 메소드")
    class Describe_findCache {

        @Test
        @DisplayName("[success] 조회된 캐시 데이터가 없는 경우 null 을 응답한다.")
        void success () {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .page(0)
                .build();

            // when
            List<FindProductListServiceResponse> result = service.findCache(command);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("[success] 조회를 요청한 페이지가 캐시 사이즈 범주에 포함되지 않는 경우 null 을 응답한다.")
        void success2 () {
            // given
            List<FindProductListServiceResponse> cacheList = new ArrayList<>();
            for(int i = 0; i<7; i++) {
                cacheList.add(FindProductListServiceResponse.builder()
                    .productId((long) i)
                    .productName("테스트 상품")
                    .price(10000)
                    .category(Category.BEAUTY)
                    .productImg("test.jpg")
                    .sellerEmail("test")
                    .build());
            }
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .page(10)
                .build();
            String redisKey = String.format("product-list::%s-%s-%s",
                command.keyword(),
                command.sortType(),
                command.category());
            redisTemplate.opsForValue().set(redisKey, jsonUtil.toJsonString(cacheList));

            // when
            List<FindProductListServiceResponse> result = service.findCache(command);

            // then
            assertThat(result).isNull();

            // clean
            redisTemplate.delete(redisKey);
        }

        @Test
        @DisplayName("[success] 슬라이싱된 캐시 사이즈가 응답해야하는 사이즈보다 작다면 null 을 응답한다.")
        void success3 () {
            // given
            List<FindProductListServiceResponse> cacheList = new ArrayList<>();
            for(int i = 0; i<7; i++) {
                cacheList.add(FindProductListServiceResponse.builder()
                    .productId((long) i)
                    .productName("테스트 상품")
                    .price(10000)
                    .category(Category.BEAUTY)
                    .productImg("test.jpg")
                    .sellerEmail("test")
                    .build());
            }
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .page(0)
                .build();
            String redisKey = String.format("product-list::%s-%s-%s",
                command.keyword(),
                command.sortType(),
                command.category());
            redisTemplate.opsForValue().set(redisKey, jsonUtil.toJsonString(cacheList));

            // when
            List<FindProductListServiceResponse> result = service.findCache(command);

            // then
            assertThat(result).isNull();

            // clean
            redisTemplate.delete(redisKey);
        }

        @Test
        @DisplayName("[success] 조회된 캐시 데이터가 있고 슬라이싱에 성공한다면 슬라이싱된 데이터를 응답한다.")
        void success4 () {
            // given
            List<FindProductListServiceResponse> cacheList = new ArrayList<>();
            for(int i = 0; i<10; i++) {
                cacheList.add(FindProductListServiceResponse.builder()
                    .productId((long) i)
                    .productName("테스트 상품")
                    .price(10000)
                    .category(Category.BEAUTY)
                    .productImg("test.jpg")
                    .sellerEmail("test")
                    .build());
            }
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .page(0)
                .build();
            String redisKey = String.format("product-list::%s-%s-%s",
                command.keyword(),
                command.sortType(),
                command.category());
            redisTemplate.opsForValue().set(redisKey, jsonUtil.toJsonString(cacheList));

            // when
            List<FindProductListServiceResponse> result = service.findCache(command);

            // then
            assertThat(result.size()).isEqualTo(10);

            // clean
            redisTemplate.delete(redisKey);
        }
    }
}
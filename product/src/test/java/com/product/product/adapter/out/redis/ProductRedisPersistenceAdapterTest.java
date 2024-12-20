package com.product.product.adapter.out.redis;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.product.IntegrationTestSupport;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.product.domain.Category;
import com.product.product.domain.ProductSortType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class ProductRedisPersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    ProductRedisPersistenceAdapter adapter;

    @Nested
    @DisplayName("[findByKeyword] 키워드로 상품리스트 캐시를 조회하는 메소드")
    class Describe_findByKeyword {

        @Test
        @DisplayName("[success] 조회된 캐시 데이터가 있다면 캐시 데이터를 응답한다.")
        void success1() {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트 키워드")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .build();
            String redisKey = String.format("product-list::%s-%s-%s",
                command.keyword().replace(" ", "-"),
                command.sortType(),
                command.category());
            FindProductListServiceResponse cacheItem = FindProductListServiceResponse.builder()
                .productId(1L)
                .productName("테스트 상품")
                .price(10000)
                .category(Category.TOTAL)
                .productImg("test.jpg")
                .sellerEmail("od@gmai.com")
                .regDateTime(LocalDateTime.now())
                .build();
            List<FindProductListServiceResponse> cacheList = new ArrayList<>();
            cacheList.add(cacheItem);
            redisTemplate.opsForValue().set(redisKey, jsonUtil.toJsonString(cacheList));

            // when
            List<FindProductListServiceResponse> result = adapter.findByKeyword(command);

            // then
            assertThat(result.size()).isEqualTo(1);
            assertThat(result.getFirst().productId()).isEqualTo(cacheItem.productId());
            assertThat(result.getFirst().productName()).isEqualTo(cacheItem.productName());
            assertThat(result.getFirst().price()).isEqualTo(cacheItem.price());
            assertThat(result.getFirst().category()).isEqualTo(cacheItem.category());
            assertThat(result.getFirst().productImg()).isEqualTo(cacheItem.productImg());
            assertThat(result.getFirst().sellerEmail()).isEqualTo(cacheItem.sellerEmail());
            assertThat(result.getFirst().regDateTime()).isEqualTo(cacheItem.regDateTime());

            // clean
            redisTemplate.delete(redisKey);
        }

        @Test
        @DisplayName("[success] 조회된 캐시 데이터가 없다면 null 을 응답한다.")
        void success2() {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트 키워드22")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .build();

            // when
            List<FindProductListServiceResponse> result = adapter.findByKeyword(command);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("[success] 캐시 데이터 조회중 예외 발생시 fallback 메소드를 실행하고 null 을 응답한다.")
        void success3(CapturedOutput output) {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("테스트 키워드")
                .sortType(ProductSortType.SALES_VOLUME)
                .category(Category.TOTAL)
                .build();
            String redisKey = String.format("product-list::%s-%s-%s",
                command.keyword().replace(" ", "-"),
                command.sortType(),
                command.category());
            redisTemplate.opsForValue().set(redisKey, "errorItem");

            // when
            List<FindProductListServiceResponse> result = adapter.findByKeyword(command);

            // then
            assertThat(result).isNull();
            assertThat(output.getOut()).contains("[findByKeywordFallback] call -");

            // clean
            redisTemplate.delete(redisKey);
            circuitBreakerRegistry.circuitBreaker("redis").reset();
        }
    }
}
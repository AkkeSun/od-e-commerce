package com.product.product.adapter.out.redis;

import com.product.global.util.JsonUtil;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.FindProductCachePort;
import com.product.product.application.service.find_product_list.FindProductListServiceResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductRedisPersistenceAdapter implements FindProductCachePort {

    private final JsonUtil jsonUtil;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findByKeywordFallback")
    public List<FindProductListServiceResponse> findByKeyword(
        FindProductListCommand command) {
        String redisKey = String.format("product-list::%s-%s-%s",
            command.keyword().replace(" ", "-"),
            command.sortType(),
            command.category());

        String redisData = redisTemplate.opsForValue().get(redisKey);
        return !StringUtils.hasText(redisData) ? null : jsonUtil
            .parseJsonToList(redisData, FindProductListServiceResponse.class);
    }

    private List<FindProductListServiceResponse> findByKeywordFallback(
        FindProductListCommand command, Exception e) {
        log.error("[findByKeywordFallback] call - " + e.getMessage());
        return null;
    }
}

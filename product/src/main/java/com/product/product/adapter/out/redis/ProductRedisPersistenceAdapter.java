package com.product.product.adapter.out.redis;

import com.product.global.util.JsonUtil;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.FindProductCachePort;
import com.product.product.domain.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductRedisPersistenceAdapter implements FindProductCachePort {

    @Value("${service-constant.product.response-page-size}")
    private int responsePageSize;
    private final JsonUtil jsonUtil;
    private final RedisTemplate<String, String> redisTemplate;


    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findByKeywordFallback")
    public LinkedHashSet<Product> findByKeyword(FindProductListCommand command) {
        String redisKey = String.format("product-list::%s-%s-%s",
            command.keyword(), command.sortType(), command.category());
        String redisData = redisTemplate.opsForValue().get(redisKey);
        if (!StringUtils.hasText(redisData)) {
            return null;
        }

        LinkedHashSet<Product> products = jsonUtil
            .parseJsonToLinkedHashSet(redisData, Product.class);

        int startIndex = command.page() * responsePageSize;
        int endIndex = Math.min(startIndex + responsePageSize, products.size());

        if (startIndex >= products.size()) {
            return new LinkedHashSet<>();
        }
        return products.stream()
            .skip(startIndex)
            .limit(endIndex - startIndex)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private LinkedHashSet<Product> findByKeywordFallback(FindProductListCommand command,
        Exception e) {
        log.error("[findByKeywordFallback] call - " + e.getMessage());
        return null;
    }
}

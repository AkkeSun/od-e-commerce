package com.product.product.adapter.out.redis;

import com.product.global.util.JsonUtil;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.FindProductCachePort;
import com.product.product.domain.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.LinkedHashSet;
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
    private String productListKey = "product-list::%s-%s-%s";


    @Override
    @CircuitBreaker(name = "redis", fallbackMethod = "findByKeywordFallback")
    public LinkedHashSet<Product> findByKeyword(FindProductListCommand command) {
        String redisKey = String.format(productListKey,
            command.keyword(), command.page(), command.sortType());
        String redisData = redisTemplate.opsForValue().get(redisKey);
        if (!StringUtils.hasText(redisData)) {
            return null;
        }
        return jsonUtil.parseJsonToLinkedHashSet(redisData, Product.class);
    }

    private LinkedHashSet<Product> findByKeywordFallback(FindProductListCommand command,
        Exception e) {
        log.error("[findByKeywordFallback] call - " + e.getMessage());
        return null;
    }
}

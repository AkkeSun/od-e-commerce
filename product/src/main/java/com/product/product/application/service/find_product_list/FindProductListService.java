package com.product.product.application.service.find_product_list;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.product.global.util.DateUtil;
import com.product.global.util.JsonUtil;
import com.product.product.application.port.in.FindProductListUseCase;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.FindProductCachePort;
import com.product.product.application.port.out.FindProductEsPort;
import com.product.product.application.port.out.FindProductVectorPort;
import com.product.product.application.port.out.ProduceProductPort;
import com.product.product.domain.Product;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class FindProductListService implements FindProductListUseCase {

    private final DateUtil dateUtil;
    private final JsonUtil jsonUtil;
    private final ProduceProductPort produceProductPort;
    private final FindProductEsPort findProductPort;
    private final FindProductCachePort findProductCachePort;
    private final FindProductVectorPort findProductVectorPort;
    @Value("${service-constant.product.response-page-size}")
    private int responsePageSize;
    @Value("${kafka.topic.product-search-keyword}")
    private String topicName;

    @Override
    public List<FindProductListServiceResponse> findProductList(FindProductListCommand command) {
        if (command.isSearchKeywordSaveNeeded()) {
            ObjectNode objectNode = jsonUtil.toObjectNode(command);
            objectNode.put("regDateTime", dateUtil.getCurrentDateTime());
            produceProductPort.sendMessage(topicName, objectNode.toString());
        }

        // ------ STEP 1 : cache search ------
        List<FindProductListServiceResponse> cache = findCache(command);
        if (cache != null) {
            log.debug("[findProductList] response from cache");
            return cache;
        }

        // ------ STEP 2 : elasticsearch ------
        LinkedHashSet<Product> products = findProductPort.findByKeyword(command);

        // ------ STEP 3 : vector search ------
        if (products.size() != responsePageSize) {
            products.addAll(findProductVectorPort.findProductList(command));
            if (command.excludeProductIds() != null) {
                products = products.stream()
                    .filter(p -> !command.excludeProductIds().contains(p.getProductId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            }
            log.debug("[findProductList] response from vector");
        }

        return products.stream()
            .limit(responsePageSize)
            .map(product -> FindProductListServiceResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .productImg(product.getProductImg())
                .category(product.getCategory())
                .sellerEmail(product.getSellerEmail())
                .regDateTime(product.getRegDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build())
            .toList();
    }

    public List<FindProductListServiceResponse> findCache(FindProductListCommand command) {

        List<FindProductListServiceResponse> cache = findProductCachePort.findByKeyword(command);
        if (cache == null) {
            return null;
        }

        int startIndex = command.page() * responsePageSize;
        int endIndex = Math.min(startIndex + responsePageSize, cache.size());
        if (startIndex >= cache.size() || endIndex > cache.size()) {
            return null;
        }

        List<FindProductListServiceResponse> slicedData = cache.subList(startIndex, endIndex);
        if (slicedData.size() < responsePageSize) {
            return null;
        }

        return slicedData;
    }
}

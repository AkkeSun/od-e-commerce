package com.product.product.application.service.find_product_list;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.product.global.util.DateUtil;
import com.product.global.util.JsonUtil;
import com.product.product.application.port.in.FindProductListUseCase;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.FindProductCachePort;
import com.product.product.application.port.out.FindProductEsPort;
import com.product.product.application.port.out.ProduceProductPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindProductListService implements FindProductListUseCase {

    private final DateUtil dateUtil;
    private final JsonUtil jsonUtil;
    private final ProduceProductPort produceProductPort;
    private final FindProductEsPort findProductPort;
    private final FindProductCachePort findProductCachePort;
    @Value("${service-constant.product.response-page-size}")
    private int responsePageSize;

    @Override
    public List<FindProductListServiceResponse> findProductList(FindProductListCommand command) {
        if (command.isSearchKeywordSaveNeeded()) {
            ObjectNode objectNode = jsonUtil.toObjectNode(command);
            objectNode.put("regDateTime", dateUtil.getCurrentDateTime());
            produceProductPort.sendMessage("product-search-keyword-topic", objectNode.toString());
        }

        LinkedHashSet<Product> products = findProductCachePort.findByKeyword(command);
        if (products == null) {
            products = getProductsByElasticSearch(command);
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
                .regDateTime(product.getRegDateTime())
                .build())
            .toList();
    }

    private LinkedHashSet<Product> getProductsByElasticSearch(FindProductListCommand command) {
        // ---------- step 1 : keyword search ----------
        LinkedHashSet<Product> products = findProductPort.findByKeyword(command);

        // ---------- step 2 : category search ----------
        if (products.size() != responsePageSize) {
            Category category = command.category();
            if (category.equals(Category.TOTAL)) {
                category = products.stream()
                    .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()))
                    .entrySet().stream()
                    .max(Comparator.comparingLong(Entry::getValue))
                    .map(Entry::getKey)
                    .orElse(null);
            }

            products.addAll(findProductPort.findByCategory(command));
            // 이미 노출한 상품 아이디 제외
            if (!command.excludeProductIds().isEmpty()) {
                products = products.stream()
                    .filter(
                        product -> command.excludeProductIds().contains(product.getProductId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            }
        }
        return products;
    }
}

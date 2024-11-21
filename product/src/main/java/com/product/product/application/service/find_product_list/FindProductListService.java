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
import com.product.product.domain.ProductSortType;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindProductListService implements FindProductListUseCase {

    private final DateUtil dateUtil;
    private final JsonUtil jsonUtil;
    private final ProduceProductPort produceProductPort;
    private final FindProductEsPort findProductPort;
    private final FindProductCachePort findProductCachePort;
    @Value("${service-constant.product.search-page-size}")
    private int searchPageSize;
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
        LinkedHashSet<Product> products = findProductPort.findByKeyword(command.keyword(),
            makePageRequest(command.page(), responsePageSize, command.sortType()));

        // ---------- step 2 : category search ----------
        if (products.size() != responsePageSize) {
            Category category = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparingLong(Entry::getValue))
                .map(Entry::getKey)
                .orElse(null);

            if (category != null) {
                products.addAll(findProductPort.findByCategory(category,
                    makePageRequest(command.page(), searchPageSize, command.sortType())));
            } else {
                products.addAll(findProductPort.findAll(
                    makePageRequest(command.page(), searchPageSize, command.sortType())));
            }
        }
        return products;
    }

    private PageRequest makePageRequest(int page, int size, ProductSortType sortType) {
        return switch (sortType) {
            case LOWEST_PRICE -> PageRequest.of(page, size, Sort.by(Direction.ASC, "price"));
            case HIGHEST_PRICE -> PageRequest.of(page, size, Sort.by(Direction.DESC, "price"));
            case SALES_VOLUME -> PageRequest.of(page, size, Sort.by(Direction.DESC, "salesCount"));
            case LATEST -> PageRequest.of(page, size, Sort.by(Direction.DESC, "productId"));
            case MOST_REVIEWS -> PageRequest.of(page, size, Sort.by(Direction.DESC, "reviewCount"));
            default -> PageRequest.of(page, size, Sort.by(Direction.DESC, "totalScore"));
        };
    }
}

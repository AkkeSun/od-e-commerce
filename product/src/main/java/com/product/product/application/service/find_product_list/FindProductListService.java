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
import java.util.LinkedHashSet;
import java.util.List;
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
    private final FindProductVectorPort findProductVectorPort;
    @Value("${service-constant.product.response-page-size}")
    private int responsePageSize;

    @Override
    public List<FindProductListServiceResponse> findProductList(FindProductListCommand command) {
        if (command.isSearchKeywordSaveNeeded()) {
            ObjectNode objectNode = jsonUtil.toObjectNode(command);
            objectNode.put("regDateTime", dateUtil.getCurrentDateTime());
            produceProductPort.sendMessage("product-search-keyword-topic", objectNode.toString());
        }

        // ------ STEP 1 : cache search------
        LinkedHashSet<Product> products = findProductCachePort.findByKeyword(command);

        // ------ STEP 2 : elasticsearch ------
        if (products == null) {
            products = findProductPort.findByKeyword(command);
        }

        // ------ STEP 3 : vector search ------
        if (products.size() != responsePageSize) {
            products.addAll(findProductVectorPort.findProductList(command));
            if (command.excludeProductIds() != null) {
                products = products.stream()
                    .filter(
                        product -> command.excludeProductIds().contains(product.getProductId()))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            }
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
}

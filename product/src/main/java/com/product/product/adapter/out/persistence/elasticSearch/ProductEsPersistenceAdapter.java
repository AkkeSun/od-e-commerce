package com.product.product.adapter.out.persistence.elasticSearch;

import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.FindProductEsPort;
import com.product.product.application.port.out.RegisterProductEsPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import com.product.product.domain.ProductSortType;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductEsPersistenceAdapter implements RegisterProductEsPort, FindProductEsPort {

    @Value("${service-constant.product.response-page-size}")
    private int responsePageSize;
    @Value("${service-constant.product.search-page-size}")
    private int searchPageSize;
    private final ProductEsDocumentMapper productMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Product register(Product product) {
        ProductEsDocument savedDocument = elasticsearchOperations
            .save(productMapper.toDocument(product));
        return productMapper.toDomain(savedDocument);
    }

    @Override
    public LinkedHashSet<Product> findByKeyword(FindProductListCommand command) {
        CriteriaQuery query;
        if (command.category().equals(Category.TOTAL)) {
            query = new CriteriaQuery(Criteria.where("productName").contains(command.keyword())
                .or(Criteria.where("description").contains(command.keyword())));
        } else {
            query = new CriteriaQuery(Criteria.where("category").is(command.category().name())
                .and(Criteria.where("productName").contains(command.keyword())
                    .or(Criteria.where("description").contains(command.keyword()))));
        }

        query.setPageable(makePageRequest(command.page(), responsePageSize, command.sortType()));
        SearchHits<ProductEsDocument> searchHits = elasticsearchOperations
            .search(query, ProductEsDocument.class);

        return searchHits.stream()
            .map(hits -> productMapper.toDomain(hits.getContent()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Product> findByCategory(FindProductListCommand command) {
        CriteriaQuery query;
        if (command.category().equals(Category.TOTAL)) {
            query = new CriteriaQuery(new Criteria());
        } else {
            query = new CriteriaQuery(Criteria.where("category").is(command.category().name()));
        }
        query.setPageable(makePageRequest(command.page(), responsePageSize, command.sortType()));
        SearchHits<ProductEsDocument> searchHits = elasticsearchOperations
            .search(query, ProductEsDocument.class);
        return searchHits.stream()
            .map(hits -> productMapper.toDomain(hits.getContent()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
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

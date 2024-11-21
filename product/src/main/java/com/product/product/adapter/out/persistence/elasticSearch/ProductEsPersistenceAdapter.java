package com.product.product.adapter.out.persistence.elasticSearch;

import com.product.product.application.port.out.FindProductEsPort;
import com.product.product.application.port.out.RegisterProductEsPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductEsPersistenceAdapter implements RegisterProductEsPort, FindProductEsPort {

    private final ProductEsDocumentMapper productMapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Product register(Product product) {
        ProductEsDocument savedDocument = elasticsearchOperations
            .save(productMapper.toDocument(product));
        return productMapper.toDomain(savedDocument);
    }

    @Override
    public LinkedHashSet<Product> findByKeyword(String keyword, PageRequest pageRequest) {
        Criteria criteria = Criteria.where("productName").contains(keyword)
            .or(Criteria.where("description").contains(keyword));
        CriteriaQuery query = new CriteriaQuery(criteria);

        query.setPageable(pageRequest);
        SearchHits<ProductEsDocument> searchHits = elasticsearchOperations
            .search(query, ProductEsDocument.class);

        return searchHits.stream()
            .map(hits -> productMapper.toDomain(hits.getContent()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Product> findByTags(Set<String> tags, PageRequest pageRequest) {
        Criteria criteria = Criteria.where("tags").in(tags);
        CriteriaQuery query = new CriteriaQuery(criteria);

        query.setPageable(pageRequest);
        SearchHits<ProductEsDocument> searchHits = elasticsearchOperations
            .search(query, ProductEsDocument.class);

        return searchHits.stream()
            .map(hits -> productMapper.toDomain(hits.getContent()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Product> findByCategory(Category category, PageRequest pageRequest) {
        Criteria criteria = Criteria.where("category").is(category.name());
        CriteriaQuery query = new CriteriaQuery(criteria);

        query.setPageable(pageRequest);
        SearchHits<ProductEsDocument> searchHits = elasticsearchOperations
            .search(query, ProductEsDocument.class);

        return searchHits.stream()
            .map(hits -> productMapper.toDomain(hits.getContent()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public LinkedHashSet<Product> findAll(PageRequest pageRequest) {
        CriteriaQuery query = new CriteriaQuery(new Criteria());

        query.setPageable(pageRequest);
        SearchHits<ProductEsDocument> searchHits = elasticsearchOperations
            .search(query, ProductEsDocument.class);

        return searchHits.stream()
            .map(hits -> productMapper.toDomain(hits.getContent()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

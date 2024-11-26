package com.product_agent.product.adapter.out.persistence.elasticSearch;

import static com.product_agent.global.exception.ErrorCode.DoesNotExist_PROUCT_INFO;

import com.product_agent.global.exception.CustomNotFoundException;
import com.product_agent.product.application.port.out.FindProductEsPort;
import com.product_agent.product.application.port.out.UpdateProductEsPort;
import com.product_agent.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse.Result;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ElasticSearchPersistenceAdapter implements FindProductEsPort, UpdateProductEsPort {

    private final ProductEsDocumentMapper mapper;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Product findById(Long productId) {
        ProductEsDocument document = elasticsearchOperations.get(
            String.valueOf(productId),
            ProductEsDocument.class,
            IndexCoordinates.of("product")
        );

        if (document == null) {
            throw new CustomNotFoundException(DoesNotExist_PROUCT_INFO);
        }
        return mapper.toDomain(document);
    }

    @Override
    public void updateSalesCount(Product product) {
        ProductEsDocument savedProduct = elasticsearchOperations.get(
            String.valueOf(product.getProductId()),
            ProductEsDocument.class,
            IndexCoordinates.of("product")
        );

        Document document = Document.create();
        document.put("salesCount", product.getSalesCount());

        UpdateQuery updateQuery = UpdateQuery.builder(String.valueOf(product.getProductId()))
            .withDocument(document)
            .build();
        Result reuslt = elasticsearchOperations.update(updateQuery,
            IndexCoordinates.of("product")).getResult();
    }
}

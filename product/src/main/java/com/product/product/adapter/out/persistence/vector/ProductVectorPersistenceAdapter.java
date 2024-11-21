package com.product.product.adapter.out.persistence.vector;

import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.FindProductVectorPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.store.embedding.filter.comparison.IsEqualTo;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductVectorPersistenceAdapter implements FindProductVectorPort {

    private final EmbeddingModel allMiniLmL6V2EmbeddingModel;
    private final ChromaEmbeddingStore embeddingStore;
    private final ProductVectorMapper productVectorMapper;
    @Value("${service-constant.product.search-page-size}")
    private int searchPageSize;

    @Override
    public LinkedHashSet<Product> findProductList(FindProductListCommand command) {
        EmbeddingSearchRequest request;
        if (command.category().equals(Category.TOTAL)) {
            request = EmbeddingSearchRequest.builder()
                .maxResults(searchPageSize)
                .queryEmbedding(allMiniLmL6V2EmbeddingModel.embed(command.keyword()).content())
                .build();
        } else {
            request = EmbeddingSearchRequest.builder()
                .maxResults(searchPageSize)
                .queryEmbedding(allMiniLmL6V2EmbeddingModel.embed(command.keyword()).content())
                .filter(new IsEqualTo("category", command.category().name()))
                .build();
        }
        
        List<Metadata> result = embeddingStore.search(request).matches()
            .stream()
            .map(match -> match.embedded().metadata())
            .toList();

        return embeddingStore.search(request).matches()
            .stream()
            .map(match -> productVectorMapper.toDomain(match.embedded().metadata()))
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}

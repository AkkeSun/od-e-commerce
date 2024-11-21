package com.product_agent.product.adapter.out.persistence.vector;

import com.product_agent.product.application.port.out.DeleteProductVectorPort;
import com.product_agent.product.application.port.out.RegisterProductVectorPort;
import com.product_agent.product.domain.Product;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductVectorPersistenceAdapter implements RegisterProductVectorPort,
    DeleteProductVectorPort {

    private final ProductVectorMapper productVectorMapper;
    private final ChromaEmbeddingStore chromaEmbeddingStore;

    @Override
    public String registerProductVector(String document, Embedding embedding, Product product) {
        try {
            Metadata metadata = productVectorMapper.toMetadata(product);
            TextSegment segment = TextSegment.from(document, metadata);
            chromaEmbeddingStore.add(embedding, segment);
            return chromaEmbeddingStore.add(embedding, segment);
        } catch (Exception e) {
            log.error("registerProductVector {} -- {}", document, e.getMessage());
            return "";
        }
    }

    @Override
    public void deleteProductVector(String vectorId) {
        chromaEmbeddingStore.remove(vectorId);
    }
}

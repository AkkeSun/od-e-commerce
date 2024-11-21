package com.product_agent.product.adapter.out.embedding;

import com.product_agent.product.application.port.out.GenerateEmbeddingPort;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.EmbeddingModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class EmbeddingAdapter implements GenerateEmbeddingPort {

    private final EmbeddingModel allMiniLmL6V2EmbeddingModel;

    @Override
    public Embedding embed(String document) {
        return allMiniLmL6V2EmbeddingModel.embed(document).content();
    }
}

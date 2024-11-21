package com.product_agent.product.application.port.out;

import dev.langchain4j.data.embedding.Embedding;

public interface GenerateEmbeddingPort {

    Embedding embed(String document);
}

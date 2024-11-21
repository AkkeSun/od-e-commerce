package com.product_agent.product.application.port.out;

import com.product_agent.product.domain.Product;
import dev.langchain4j.data.embedding.Embedding;

public interface RegisterProductVectorPort {

    String registerProductVector(String document, Embedding embedding, Product product);
}

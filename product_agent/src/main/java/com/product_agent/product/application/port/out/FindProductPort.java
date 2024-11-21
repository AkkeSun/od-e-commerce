package com.product_agent.product.application.port.out;

import com.product_agent.product.domain.Product;
import java.util.List;

public interface FindProductPort {

    List<Product> findByEmbeddingYn(String embeddingYn);
}

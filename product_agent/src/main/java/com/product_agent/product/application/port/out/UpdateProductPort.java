package com.product_agent.product.application.port.out;

public interface UpdateProductPort {

    boolean updateEmbeddingYnByProductId(Long productId, String yn);
}

package com.product_agent.product.adapter.out.persistence.jpa;

import com.product_agent.global.util.ShardKeyUtil;
import com.product_agent.product.adapter.out.persistence.jpa.shard1.ProductShard1Adapter;
import com.product_agent.product.adapter.out.persistence.jpa.shard2.ProductShard2Adapter;
import com.product_agent.product.application.port.out.FindProductPort;
import com.product_agent.product.application.port.out.UpdateProductPort;
import com.product_agent.product.domain.Product;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class ProductPersistenceAdapter implements FindProductPort, UpdateProductPort {

    private final ShardKeyUtil shardKeyUtil;
    private final ProductShard1Adapter productShard1Adapter;
    private final ProductShard2Adapter productShard2Adapter;

    @Override
    public List<Product> findByEmbeddingYn(String embeddingYn) {
        List<Product> products = new ArrayList<>();
        products.addAll(productShard1Adapter.findByEmbeddingYn(embeddingYn));
        products.addAll(productShard2Adapter.findByEmbeddingYn(embeddingYn));
        return products;
    }

    @Override
    public boolean updateEmbeddingYnByProductId(Long productId, String yn) {
        if (shardKeyUtil.isShard1(productId)) {
            return productShard1Adapter.updateEmbeddingYnByProductId(productId, yn);
        } else {
            return productShard2Adapter.updateEmbeddingYnByProductId(productId, yn);
        }
    }
}

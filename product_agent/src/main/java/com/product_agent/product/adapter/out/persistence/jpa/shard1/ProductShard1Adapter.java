package com.product_agent.product.adapter.out.persistence.jpa.shard1;

import com.product_agent.product.application.port.out.FindProductPort;
import com.product_agent.product.application.port.out.UpdateProductPort;
import com.product_agent.product.domain.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional("primaryTransactionManager")
public class ProductShard1Adapter implements FindProductPort, UpdateProductPort {

    private final ProductShard1Repository productRepository;
    private final ProductShard1Mapper productMapper;

    @Override
    public List<Product> findByEmbeddingYn(String embeddingYn) {
        return productRepository.findByEmbeddingYn(embeddingYn).stream()
            .map(productMapper::toDomain)
            .toList();
    }

    @Override
    public boolean updateEmbeddingYnByProductId(Long productId, String yn) {
        try {
            productRepository.updateEmbeddingYnByProductId(productId, yn);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

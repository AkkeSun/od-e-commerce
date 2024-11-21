package com.product.product.adapter.out.persistence.jpa;

import com.product.global.util.ShardKeyUtil;
import com.product.product.adapter.out.persistence.jpa.shard1.ProductShard1Adapter;
import com.product.product.adapter.out.persistence.jpa.shard2.ProductShard2Adapter;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class ProductPersistenceAdapter implements RegisterProductPort, FindProductPort {

    private final ShardKeyUtil shardKeyUtil;
    private final ProductShard1Adapter productShard1Adapter;
    private final ProductShard2Adapter productShard2Adapter;

    @Override
    public Product register(Product product) {
        return shardKeyUtil.isShard1(product.getProductId()) ?
            productShard1Adapter.register(product) : productShard2Adapter.register(product);
    }

    @Override
    public Product findById(Long id) {
        return shardKeyUtil.isShard1(id) ?
            productShard1Adapter.findById(id) : productShard2Adapter.findById(id);
    }
}

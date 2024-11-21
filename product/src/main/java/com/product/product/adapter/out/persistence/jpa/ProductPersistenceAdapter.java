package com.product.product.adapter.out.persistence.jpa;

import com.product.global.exception.CustomNotFoundException;
import com.product.global.exception.ErrorCode;
import com.product.global.util.ShardKeyUtil;
import com.product.product.adapter.out.persistence.jpa.shard1.ProductShard1Entity;
import com.product.product.adapter.out.persistence.jpa.shard1.ProductShard1Repository;
import com.product.product.adapter.out.persistence.jpa.shard2.ProductShard2Entity;
import com.product.product.adapter.out.persistence.jpa.shard2.ProductShard2Repository;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductPersistenceAdapter implements RegisterProductPort, FindProductPort {

    private final ProductMapper productMapper;
    private final ShardKeyUtil shardKeyUtil;
    private final ProductShard1Repository productShard1Repository;
    private final ProductShard2Repository productShard2Repository;

    @Override
    public Product register(Product product) {
        if (shardKeyUtil.isShard1(product.getProductId())) {
            ProductShard1Entity entity = productMapper.toShard1Entity(product);
            return productMapper.toDomain(productShard1Repository.save(entity));
        }

        ProductShard2Entity entity = productMapper.toShard2Entity(product);
        return productMapper.toDomain(productShard2Repository.save(entity));
    }

    @Override
    public Product findById(Long id) {
        if (shardKeyUtil.isShard1(id)) {
            ProductShard1Entity entity = productShard1Repository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));
            return productMapper.toDomain(entity);
        }

        ProductShard2Entity entity = productShard2Repository.findById(id)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));
        return productMapper.toDomain(entity);
    }
}

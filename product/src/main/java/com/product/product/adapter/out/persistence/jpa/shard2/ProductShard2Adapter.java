package com.product.product.adapter.out.persistence.jpa.shard2;

import com.product.global.exception.CustomNotFoundException;
import com.product.global.exception.ErrorCode;
import com.product.global.util.ShardKeyUtil;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional("secondaryTransactionManager")
public class ProductShard2Adapter implements RegisterProductPort, FindProductPort {

    private final ProductShard2Mapper productMapper;
    private final ShardKeyUtil shardKeyUtil;
    private final ProductShard2Repository productRepository;

    @Override
    public Product register(Product product) {
        ProductShard2Entity entity = productMapper.toShard2Entity(product);
        return productMapper.toDomain(productRepository.save(entity));
    }

    @Override
    public Product findById(Long id) {
        ProductShard2Entity entity = productRepository.findById(id)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));
        return productMapper.toDomain(entity);
    }
}

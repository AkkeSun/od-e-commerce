package com.product.product.adapter.out.persistence.jpa;

import com.product.global.util.ShardKeyUtil;
import com.product.product.adapter.out.persistence.jpa.shard1.ProductShard1Adapter;
import com.product.product.adapter.out.persistence.jpa.shard2.ProductShard2Adapter;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.application.port.out.UpdateProductPort;
import com.product.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class ProductPersistenceAdapter implements FindProductPort,
    RegisterProductPort, UpdateProductPort, DeleteProductPort {

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

    @Override
    public Product updateProductSaleInfo(Product product, Long accountId, int productCount) {
        return shardKeyUtil.isShard1(product.getProductId()) ?
            productShard1Adapter.updateProductSaleInfo(product, accountId, productCount) :
            productShard2Adapter.updateProductSaleInfo(product, accountId, productCount);
    }

    @Override
    public Product updateProductQuantity(Product product, int quantity) {
        return shardKeyUtil.isShard1(product.getProductId()) ?
            productShard1Adapter.updateProductQuantity(product, quantity) :
            productShard2Adapter.updateProductQuantity(product, quantity);
    }

    @Override
    public void deleteById(Long productId) {
        if (shardKeyUtil.isShard1(productId)) {
            productShard1Adapter.deleteById(productId);
        } else {
            productShard2Adapter.deleteById(productId);
        }
    }

    @Override // for test
    public void deleteAll() {
        productShard1Adapter.deleteAll();
        productShard2Adapter.deleteAll();
    }
}

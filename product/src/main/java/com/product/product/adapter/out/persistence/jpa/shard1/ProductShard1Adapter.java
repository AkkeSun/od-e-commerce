package com.product.product.adapter.out.persistence.jpa.shard1;

import com.product.global.exception.CustomNotFoundException;
import com.product.global.exception.ErrorCode;
import com.product.global.util.DateUtil;
import com.product.global.util.SnowflakeGenerator;
import com.product.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.application.port.out.UpdateProductPort;
import com.product.product.domain.HistoryType;
import com.product.product.domain.Product;
import com.product.product.domain.ProductHistory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional("primaryTransactionManager")
public class ProductShard1Adapter implements RegisterProductPort, FindProductPort,
    UpdateProductPort, DeleteProductPort {

    private final DateUtil dateUtil;
    private final SnowflakeGenerator snowflakeGenerator;
    private final ProductShard1Mapper productMapper;
    private final ProductShard1Repository productRepository;
    private final ProductHistoryShard1Repository productHistoryRepository;

    @Override
    public Product register(Product product) {
        ProductShard1Entity entity = productMapper.toEntity(product);
        return productMapper.toDomain(productRepository.save(entity));
    }

    @Override
    public Product findById(Long id) {
        ProductShard1Entity entity = productRepository.findById(id)
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));
        return productMapper.toDomain(entity);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }

    @Override
    public List<ProductHistory> findHistoryByProductIdAndAccountId(Long productId, Long accountId) {
        return productHistoryRepository
            .findByProductIdAndAccountId(productId, accountId).stream()
            .map(productMapper::toDomain)
            .toList();
    }

    @Override
    public Product updateProductQuantity(Product product, Long accountId,
        UpdateProductQuantityCommand command) {
        productRepository.updateSalesCountAndQuantity(product);
        productHistoryRepository.save(ProductHistoryShard1Entity.builder()
            .id(snowflakeGenerator.nextId())
            .productId(product.getProductId())
            .accountId(accountId)
            .type(HistoryType.valueOf(command.updateType().name()))
            .detailInfo(String.valueOf(command.productCount()))
            .regDate(dateUtil.getCurrentDate())
            .regDateTime(LocalDateTime.now())
            .build());
        return product;
    }

    @Override
    public void deleteById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
        productHistoryRepository.deleteAll();
    }
}

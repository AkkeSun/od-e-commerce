package com.product.product.application.port.out;

import com.product.product.domain.Product;
import com.product.product.domain.ProductHistory;
import java.util.List;

public interface FindProductPort {

    Product findById(Long id);

    boolean existsById(Long id);

    List<ProductHistory> findHistoryByProductIdAndAccountId(Long productId, Long accountId);
}

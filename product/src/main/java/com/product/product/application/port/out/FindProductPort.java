package com.product.product.application.port.out;

import com.product.product.domain.Product;
import com.product.product.domain.ProductHistory;

public interface FindProductPort {

    Product findById(Long id);

    ProductHistory findHistoryByProductIdAndAccountId(Long productId, Long accountId);
}

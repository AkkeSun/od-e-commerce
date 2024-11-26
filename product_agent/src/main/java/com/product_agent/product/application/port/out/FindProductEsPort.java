package com.product_agent.product.application.port.out;

import com.product_agent.product.domain.Product;

public interface FindProductEsPort {

    Product findById(Long productId);
}

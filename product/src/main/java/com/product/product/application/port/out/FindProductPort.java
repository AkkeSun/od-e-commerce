package com.product.product.application.port.out;

import com.product.product.domain.Product;

public interface FindProductPort {

    Product findById(Long id);
}

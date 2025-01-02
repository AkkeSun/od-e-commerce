package com.product.product.application.port.out;

import com.product.product.domain.Product;
import com.product.product.domain.ProductHistory;

public interface RegisterProductPort {

    Product register(Product product);

    ProductHistory registerHistory(ProductHistory productHistory);
}

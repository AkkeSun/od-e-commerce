package com.product.product.application.port.out;

import com.product.product.domain.Product;

public interface UpdateProductPort {

    Product updateProductSaleInfo(Product product, Long accountId, int productCount);

    Product updateProductQuantity(Product product, int quantity);
}

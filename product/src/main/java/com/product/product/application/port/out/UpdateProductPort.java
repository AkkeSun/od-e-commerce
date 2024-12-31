package com.product.product.application.port.out;

import com.product.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.product.domain.Product;

public interface UpdateProductPort {

    Product updateProductQuantity(Product product, Long accountId,
        UpdateProductQuantityCommand command);
}

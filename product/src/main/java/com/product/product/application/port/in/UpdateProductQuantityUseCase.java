package com.product.product.application.port.in;

import com.product.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;

public interface UpdateProductQuantityUseCase {

    UpdateProductQuantityServiceResponse updateProductQuantity(
        UpdateProductQuantityCommand command);
}

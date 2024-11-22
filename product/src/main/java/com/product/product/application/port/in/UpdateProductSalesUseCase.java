package com.product.product.application.port.in;

import com.product.product.application.port.in.command.UpdateProductSalesCommand;
import com.product.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;

public interface UpdateProductSalesUseCase {

    UpdateProductQuantityServiceResponse updateProductSales(UpdateProductSalesCommand command);
}

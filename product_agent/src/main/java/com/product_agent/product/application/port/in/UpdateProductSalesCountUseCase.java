package com.product_agent.product.application.port.in;

import com.product_agent.product.application.service.update_product_sales_count.UpdateProductSalesCountServiceResponse;

public interface UpdateProductSalesCountUseCase {

    UpdateProductSalesCountServiceResponse updateProductSalesCount(String payload);
}

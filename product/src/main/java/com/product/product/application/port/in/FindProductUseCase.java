package com.product.product.application.port.in;

import com.product.product.application.service.find_product.FindProductServiceResponse;

public interface FindProductUseCase {

    FindProductServiceResponse findProduct(Long productId);
}

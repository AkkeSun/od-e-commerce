package com.product.product.application.port.in;

import com.product.product.application.service.delete_product.DeleteProductServiceResponse;

public interface DeleteProductUseCase {

    DeleteProductServiceResponse deleteProduct(Long productId, String authentication);
}

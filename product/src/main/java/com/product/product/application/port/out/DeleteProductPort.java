package com.product.product.application.port.out;

public interface DeleteProductPort {

    void deleteById(Long productId);

    void deleteAll();
}

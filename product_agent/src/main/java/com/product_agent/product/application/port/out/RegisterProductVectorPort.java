package com.product_agent.product.application.port.out;

import com.product_agent.product.domain.Product;

public interface RegisterProductVectorPort {

    String registerProductVector(String document, Product product);
}

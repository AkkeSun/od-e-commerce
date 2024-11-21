package com.product.product.application.port.out;

import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.domain.Product;
import java.util.LinkedHashSet;

public interface FindProductVectorPort {

    LinkedHashSet<Product> findProductList(FindProductListCommand command);
}

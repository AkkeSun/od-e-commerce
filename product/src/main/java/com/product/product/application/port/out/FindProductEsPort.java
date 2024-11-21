package com.product.product.application.port.out;

import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.domain.Product;
import java.util.LinkedHashSet;

public interface FindProductEsPort {

    LinkedHashSet<Product> findByKeyword(FindProductListCommand command);

    LinkedHashSet<Product> findByCategory(FindProductListCommand command);
}

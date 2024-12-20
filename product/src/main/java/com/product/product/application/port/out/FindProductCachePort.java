package com.product.product.application.port.out;

import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.service.find_product_list.FindProductListServiceResponse;
import java.util.List;

public interface FindProductCachePort {

    List<FindProductListServiceResponse> findByKeyword(FindProductListCommand command);
}

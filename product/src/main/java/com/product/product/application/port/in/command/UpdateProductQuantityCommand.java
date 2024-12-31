package com.product.product.application.port.in.command;

import com.product.product.domain.QuantityType;
import lombok.Builder;

@Builder
public record UpdateProductQuantityCommand(
    Long productId,
    Integer productCount,
    QuantityType updateType,
    String authorization
) {

}

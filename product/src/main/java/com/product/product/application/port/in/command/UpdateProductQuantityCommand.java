package com.product.product.application.port.in.command;

import lombok.Builder;

@Builder
public record UpdateProductQuantityCommand(
    Long productId,
    Integer productCount,
    boolean isPurchased,
    String authorization
) {

}

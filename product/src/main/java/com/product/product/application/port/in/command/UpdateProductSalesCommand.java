package com.product.product.application.port.in.command;

import lombok.Builder;

@Builder
public record UpdateProductSalesCommand(
    Long productId,
    Integer productCount,
    boolean isSale,
    String authorization
) {

}

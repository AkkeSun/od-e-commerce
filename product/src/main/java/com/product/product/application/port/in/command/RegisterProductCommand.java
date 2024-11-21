package com.product.product.application.port.in.command;

import java.util.List;
import lombok.Builder;

@Builder
public record RegisterProductCommand(
    String authorization,
    String productName,
    String productImg,
    List<String> productOption,
    String description,
    long price,
    long quantity,
    String category
) {

}

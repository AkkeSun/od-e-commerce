package com.product.product.adapter.in.update_product_quantity;

import com.product.global.validation.groups.ValidationGroups.SizeGroups;
import com.product.product.application.port.in.command.UpdateProductQuantityCommand;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class UpdateProductQuantityRequest {

    @Min(value = 1, message = "상품 수량은 1 이상 이어야 합니다", groups = SizeGroups.class)
    private int productCount;

    private Boolean isPurchased;

    @Builder
    public UpdateProductQuantityRequest(int productCount, Boolean isPurchased) {
        this.productCount = productCount;
        this.isPurchased = isPurchased;
    }

    UpdateProductQuantityCommand toCommand(Long productId, String authorization) {
        return UpdateProductQuantityCommand.builder()
            .productCount(productCount)
            .isPurchased(isPurchased != null && isPurchased)
            .productId(productId)
            .authorization(authorization)
            .build();
    }
}

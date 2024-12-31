package com.product.product.adapter.in.update_product_quantity;

import com.product.global.validation.UpdateQuantityType;
import com.product.global.validation.groups.ValidationGroups.CustomGroups;
import com.product.global.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.global.validation.groups.ValidationGroups.SizeGroups;
import com.product.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.product.domain.QuantityType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class UpdateProductQuantityRequest {

    @Min(value = 1, message = "상품 수량은 1 이상 이어야 합니다", groups = SizeGroups.class)
    private int productCount;

    @UpdateQuantityType(groups = CustomGroups.class)
    @NotBlank(message = "수정 타입은 필수 값 입니다", groups = NotBlankGroups.class)
    private String updateType;

    @Builder
    public UpdateProductQuantityRequest(int productCount, String updateType) {
        this.productCount = productCount;
        this.updateType = updateType;
    }

    UpdateProductQuantityCommand toCommand(Long productId, String authorization) {
        return UpdateProductQuantityCommand.builder()
            .productCount(productCount)
            .updateType(QuantityType.valueOf(updateType))
            .productId(productId)
            .authorization(authorization)
            .build();
    }
}

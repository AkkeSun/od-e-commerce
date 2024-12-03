package com.product.product.adapter.in.update_product_quantity;

import com.product.global.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.global.validation.groups.ValidationGroups.SizeGroups;
import com.product.product.application.port.in.command.UpdateProductSalesCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class UpdateProductQuantityRequest {

    @Min(value = 1, message = "상품 수량은 1 이상 이어야 합니다", groups = SizeGroups.class)
    private int productCount;

    @NotNull(message = "판매 여부는 필수값 입니다", groups = NotBlankGroups.class)
    private Boolean isSale;

    @Builder
    public UpdateProductQuantityRequest(int productCount, Boolean isSale) {
        this.productCount = productCount;
        this.isSale = isSale;
    }

    UpdateProductSalesCommand toCommand(Long productId, String authorization) {
        return UpdateProductSalesCommand.builder()
            .productCount(productCount)
            .isSale(isSale)
            .productId(productId)
            .authorization(authorization)
            .build();
    }
}

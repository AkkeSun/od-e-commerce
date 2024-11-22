package com.product.product.adapter.in.update_product_quantity;

import com.product.global.exception.CustomValidationException;
import com.product.product.application.port.in.command.UpdateProductSalesCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class UpdateProductQuantityRequest {

    @NotBlank(message = "상품 수량은 필수값 입니다")
    private Integer productCount;

    @NotBlank(message = "판매 여부는 필수값 입니다")
    private Boolean isSale;

    @Builder
    public UpdateProductQuantityRequest(int productCount, Boolean isSale) {
        this.productCount = productCount;
        this.isSale = isSale;
    }

    void validation() {
        if (productCount < 1) {
            throw new CustomValidationException("상품 수량은 1 이상 이어야 합니다");
        }
    }

    UpdateProductSalesCommand toCommand(Long productId, String authentication) {
        return UpdateProductSalesCommand.builder()
            .productCount(productCount)
            .isSale(isSale)
            .productId(productId)
            .authentication(authentication)
            .build();
    }
}

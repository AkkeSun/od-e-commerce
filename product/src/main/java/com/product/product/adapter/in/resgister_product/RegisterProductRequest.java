package com.product.product.adapter.in.resgister_product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.global.exception.CustomValidationException;
import com.product.product.application.port.in.command.RegisterProductCommand;
import com.product.product.domain.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterProductRequest {

    @NotBlank(message = "상품명은 필수값 입니다")
    private String productName;

    @NotBlank(message = "상품 이미지는 필수값 입니다")
    private String productImg;

    @NotBlank(message = "상품 설명은 필수값 입니다")
    private String description;

    List<String> productOption;

    @Min(value = 1, message = "금액은 필수값 입니다")
    private long price;

    @Min(value = 20, message = "상품 수량은 20 이상이어야 합니다")
    private long quantity;

    @NotBlank(message = "카테고리는 필수값 입니다")
    private String category;

    @Builder
    public RegisterProductRequest(String productName, String productImg, String description,
        List<String> productOption, long price, long quantity, String category) {
        this.productName = productName;
        this.productImg = productImg;
        this.description = description;
        this.productOption = productOption;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    void validate() {
        try {
            Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new CustomValidationException("유효한 카테고리가 아닙니다.");
        }
    }

    RegisterProductCommand toCommand(String autorization) {
        return RegisterProductCommand.builder()
            .authorization(autorization)
            .productName(productName)
            .productImg(productImg)
            .productOption(productOption)
            .description(description)
            .price(price)
            .quantity(quantity)
            .category(category)
            .build();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}

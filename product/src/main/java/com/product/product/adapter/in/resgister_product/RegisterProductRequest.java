package com.product.product.adapter.in.resgister_product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.global.validation.ValidCategory;
import com.product.global.validation.ValidTextColumn;
import com.product.global.validation.groups.ValidationGroups.CustomGroups;
import com.product.global.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.global.validation.groups.ValidationGroups.SizeGroups;
import com.product.product.application.port.in.command.RegisterProductCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterProductRequest {

    List<String> productOption;

    @NotBlank(message = "상품 이미지는 필수값 입니다", groups = NotBlankGroups.class)
    @Size(max = 50, message = "상품 이미지는 50자 이하여야 합니다", groups = SizeGroups.class)
    private String productImg;

    @ValidCategory(groups = CustomGroups.class)
    @NotBlank(message = "카테고리는 필수값 입니다", groups = SizeGroups.class)
    private String category;

    @Size(max = 50, message = "상품명은 50자 이하여야 합니다", groups = SizeGroups.class)
    @NotBlank(message = "상품명은 필수값 입니다", groups = NotBlankGroups.class)
    private String productName;
    
    @NotBlank(message = "상품 설명은 필수값 입니다", groups = NotBlankGroups.class)
    @ValidTextColumn(message = "상품 설명의 최대 글자수를 초과하였습니다", groups = CustomGroups.class)
    private String description;

    @Min(value = 1, message = "금액은 필수값 입니다", groups = SizeGroups.class)
    private long price;

    @Min(value = 20, message = "상품 수량은 20 이상이어야 합니다", groups = SizeGroups.class)
    private long quantity;


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

package com.product.product.adapter.in.find_product_list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.global.exception.CustomValidationException;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.domain.Category;
import com.product.product.domain.ProductSortType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
class FindProductListRequest {

    @NotBlank(message = "검색어는 필수값 입니다")
    private String keyword;

    @NotBlank(message = "정렬 타입은 필수값 입니다")
    private String sortType;

    private int page;

    private String category;

    @Builder
    FindProductListRequest(String keyword, String sortType, int page, String category) {
        this.keyword = keyword;
        this.sortType = sortType;
        this.page = page;
        this.category = category;
    }

    void validation() {
        try {
            ProductSortType.valueOf(sortType);
        } catch (IllegalArgumentException e) {
            throw new CustomValidationException("유효하지 않은 정렬 타입 입니다");
        }
        if (StringUtils.hasText(category)) {
            try {
                Category.valueOf(category);
            } catch (IllegalArgumentException e) {
                throw new CustomValidationException("존재하지 않은 카테고리 입니다");
            }
        }
    }

    FindProductListCommand toCommand() {
        return FindProductListCommand.builder()
            .keyword(keyword)
            .sortType(ProductSortType.valueOf(sortType))
            .category(StringUtils.hasText(category) ? Category.valueOf(category) : Category.TOTAL)
            .page(page)
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

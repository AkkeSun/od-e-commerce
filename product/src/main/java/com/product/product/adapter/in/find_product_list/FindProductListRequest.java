package com.product.product.adapter.in.find_product_list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.domain.ProductSortType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindProductListRequest {

    @NotBlank(message = "검색어는 필수값 입니다")
    private String keyword;

    @NotBlank(message = "정렬 타입은 필수값 입니다")
    private String sortType;

    private int page;

    @Builder
    FindProductListRequest(String keyword, String sortType, int page) {
        this.keyword = keyword;
        this.sortType = sortType;
        this.page = page;
    }

    FindProductListCommand toCommand() {
        return FindProductListCommand.builder()
            .keyword(keyword)
            .sortType(ProductSortType.valueOf(sortType))
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
